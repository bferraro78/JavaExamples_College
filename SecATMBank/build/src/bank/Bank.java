package src.bank;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Created by Troy on 11/30/16.
 */
class Bank {
    private final String hostName;
    private final Integer portNumber;
    private final SecretKey encryptionKey;
    private final SecretKey DBencryptionKey;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InetAddress sessionAddress;
    private String sessionToken;
    private final String authFileName;
    
    /* Database */
    private Map<String, String> accToCardFileName = new HashMap<String, String>();
    private Map<String, String> accToBalance = new HashMap<String, String>();
    private Map<String, String> cardFileNameToCardNum = new HashMap<String, String>();
    private Map<String, String> tempAccToCardFileName = new HashMap<String, String>();
    private Map<String, String> tempAccToBalance = new HashMap<String, String>();
    private Map<String, String> tempCardFileNameToCardNum = new HashMap<String, String>();
    
    protected Bank (String hostName, Integer portNumber, String authFileName) throws Exception {
          this.hostName = hostName;
          this.portNumber = portNumber;
          this.encryptionKey = generateEncryptionKey();
          this.DBencryptionKey = generateEncryptionKey();
          this.authFileName = authFileName;
          createAuthFile();
          initServer();
    }
    // AES-128 key
    private SecretKey generateEncryptionKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }
    
    private String encryptMessage (String message, SecretKey encryptionKey) throws Exception {
        byte[] messageByteArr = message.getBytes();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        byte[] cipherTextByteArr = cipher.doFinal(messageByteArr);
        return Base64.getEncoder().encodeToString(cipherTextByteArr);
    }
    
    private String decryptMessage (String encryptedMessage, SecretKey encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, cipher.getParameters());
        byte[] messageByteArray = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(messageByteArray);
    }
    
    private void initServer() throws Exception {
        InetAddress addr = null;
        ServerSocket ss = null;
        Socket cs = null;
        tempAccToCardFileName = deepCopy(accToCardFileName);
        tempAccToBalance = deepCopy(accToBalance);
        tempCardFileNameToCardNum = deepCopy(cardFileNameToCardNum);
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            addr = InetAddress.getByName(hostName);
            ss = new ServerSocket(portNumber, 1 , addr);
            cs = ss.accept();
        } catch (Exception e) {
            System.exit(63);
        } finally {
            serverSocket = ss;
            clientSocket = cs;
            clientSocket.setSoTimeout(10 * 1000);
            ATMComms();
        }
    }
    
    /**
     * Creates Auth File
     */
    private void createAuthFile () {
        try {
            File authFile = new File(authFileName);
            if (authFile.exists()) {
                System.exit(255);
            }
            final char[] encodedKey = Hex.encodeHex(encryptionKey.getEncoded());
            String fileContents = String.valueOf(encodedKey);
            FileUtils.writeStringToFile(authFile, fileContents);
            System.out.println("created");
            System.out.flush();
        } catch (Exception e) {
            System.exit(255);
        }
    }
    private boolean verifySessionAddress (InetAddress address) {
        return Arrays.equals(sessionAddress.getAddress(), address.getAddress());
    }
    private boolean verifySessionToken (String token) {
        return token.equals(sessionToken);
    }
    
    /**
     * Send & receive ATM messages
     * if the bank observes it, it should print "protocol_error" to stdout (followed by a newline)
     * and rollback the current transaction.
     * Only make changes to temporary dataStructures
     */
    private void ATMComms () throws Exception {
        try {
            final PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line = "";
            String decryptline = "";
            boolean transactionSuccess = false;
            // Take care of connections
            while (!(line.equals("end"))) {
                decryptline = in.readLine();
                if (decryptline == null) {
                    break;
                }
                line = decryptMessage(decryptline, encryptionKey);
                if (line == null) {
                    break;
                }
                String[] splitArray = line.split(" ");
                String step = splitArray[0].toString();
                 /* Check Steps*/
                if (step.equals("step1")) { // Check if account exists or doesn't exist (true or false)
                    if (!verifySessionToken(splitArray[3])) {
                        break;
                    }
                    if (!verifySessionAddress(clientSocket.getInetAddress())) {
                        break; //Socket compromised - End Communications
                    }
                    String acc = splitArray[1].toString();
                    String action_flag = splitArray[2].toString();
                    String bool = checkAccount(acc, action_flag);
                    
                    bool = encryptMessage(bool, encryptionKey);
                    out.println(bool);
                    out.flush();
                } else if (step.equals("step2")) { // (false, true, or balance if -g)
                    if (!verifySessionToken(splitArray[6])) {
                        break;
                    }
                    if (!verifySessionAddress(clientSocket.getInetAddress())) {
                        break; //Socket compromised - End Communications
                    }
                    String cardName = splitArray[1].toString();
                    String cardNum = splitArray[2].toString();
                    String acc = splitArray[3].toString();
                    String action = splitArray[4].toString();
                    String action_flag = splitArray[5].toString();
                    String ret = checkCardORAddAccount(action_flag, action, acc, cardNum, cardName);
                    
                    /* PRINT BANK JSON */
                    if (!(ret.equals("false"))) {
                        processArgument(action_flag, acc, action); // pass in the flag and account name
                        transactionSuccess = true;
                    }
                    ret = encryptMessage(ret, encryptionKey);
                    out.println(ret);
                    out.flush();
                } else if (step.equals("init_check")) {
                    String initAuthCheck = splitArray[1].toString(); // Should be "hello"
                    String ret;
                    if (initAuthCheck.equals("hello")) {
                        sessionAddress = clientSocket.getInetAddress();
                        sessionToken = UUID.randomUUID().toString();
                        ret = "true" + " " + sessionToken;
                    } else {
                        break; // Protocol violation - end communications
                    }
                    ret = encryptMessage(ret, encryptionKey);
                    out.println(ret);
                    out.flush();
                } else {
                    out.println(encryptMessage("end", encryptionKey));
                    out.flush();
                    if (transactionSuccess) {
                        finalizeTransaction();
                    }
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("protocol_error");
            System.out.flush();
        } catch (InterruptedException e) {
            System.out.println("protocol_error");
            System.out.flush();
        } catch (IOException e) {
            System.out.println("protocol_error");
            System.out.flush();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid ATM message supplied");
            System.out.println("protocol_error");
            System.out.flush();
        } catch (Exception e) {
            System.err.println("Unkown error during ATM communication. Reseting socket");
            System.err.println(e.getMessage());
            System.out.println("protocol_error");
            System.out.flush();
        } finally {
            initServer(); // re-open server socket for new connections
        }
    }
    /**
     * If -n (creating a new account)
     *      - We know account alrdy does not exist, so fill hashtables with account info -- 
     *          1.starting balance 2. Card Name 3. Card Number
     *      - return "true"
     * If -d/-w/-g
     *      - Check to see if card's name correlates to account, then check
     *          to see if card's number correlates to the card file name
     *      - Return false if so else, carry out action
     *      - -g will return the balance instead of "true"
     * @throws Exception 
     */
    protected String checkCardORAddAccount (String action_flag, String action, 
            String acc, String cardNum, String cardName) throws Exception {     
        
        /* Encrypt values to access DB easier*/
        String encAcc = encryptMessage(acc, DBencryptionKey);
        String encCardNum = encryptMessage(cardNum, DBencryptionKey);
        
        /* Since account does not exist, not must add all info to all hashes */
        if (action_flag.equals("-n")) {
            tempAccToCardFileName.put(encAcc, cardName);
            tempAccToBalance.put(encAcc, action); // action is starting balance
            tempCardFileNameToCardNum.put(cardName, encCardNum);
            return "true";
        } else { // flag = -w, -d, -g
            
            /* Must first check if cardName and Number correlate to our account*/
            if (action_flag.equals("-d")) { 
                
                /* Decrypt things in DB to compare to ATM input */
                String RealCardFileName = tempAccToCardFileName.get(encAcc);
                String RealencCardNum = tempCardFileNameToCardNum.get(RealCardFileName);
                String RealCardNum = decryptMessage(RealencCardNum, DBencryptionKey);
                
                if (RealCardFileName.equals(cardName) && RealCardNum.equals(cardNum)) {
                    /* Deposit into account balance (amount of 'action' variable) */
                    String tmpBal = tempAccToBalance.get(encAcc);
                    Double tmp = Double.parseDouble(tmpBal);
                    Double newBal = tmp+Double.parseDouble(action);
                    newBal = Math.round(newBal*100.0)/100.0;
                    String newBalStr = Double.toString(newBal); 

                    tempAccToBalance.put(encAcc, newBalStr);
                    return "true";
                } else {
                    return "false";
                }
            } else if(action_flag.equals("-w")) {
                /* Decrypt things in DB to compare to ATM input */
                String RealCardFileName = tempAccToCardFileName.get(encAcc);
                String RealencCardNum = tempCardFileNameToCardNum.get(RealCardFileName);
                String RealCardNum = decryptMessage(RealencCardNum, DBencryptionKey);
                
                if (RealCardFileName.equals(cardName) && RealCardNum.equals(cardNum)) {
                    /* Withdraw from account balance (amount of 'action' variable) */
                    String tmpBal = tempAccToBalance.get(encAcc);
                    Double tmp = Double.parseDouble(tmpBal);

                    Double newBal = tmp-Double.parseDouble(action);
                    newBal = Math.round(newBal * 100.0)/100.0;
                    // Round to decimal places?
                    String newBalStr = Double.toString(newBal); 
                    if (newBal < 0.0) {
                        return "false";
                    }
                    tempAccToBalance.put(encAcc, newBalStr);
                    return "true";
                } else {
                    return "false";
                }
                
            } else { // -g
                /* Decrypt things in DB to compare to ATM input */
                String RealCardFileName = tempAccToCardFileName.get(encAcc);
                String RealencCardNum = tempCardFileNameToCardNum.get(RealCardFileName);
                String RealCardNum = decryptMessage(RealencCardNum, DBencryptionKey);
                
                if (RealCardFileName.equals(cardName) && RealCardNum.equals(cardNum)) {
                    /* Deposit into account balance - amount of 'action' variable*/
                    String tmpBal = tempAccToBalance.get(encAcc);
                    return tmpBal; // Return balance
                } else {
                    return "false";
                }
            }
        }
    }
    
    protected String checkAccount (String acc, String action_flag) throws Exception {
        String encAcc = encryptMessage(acc, DBencryptionKey);
        if (action_flag.equals("-n")) { // Check if account already exists with that name, if so FALSE
            if (tempAccToCardFileName.containsKey(encAcc)) {
                return "false";
            } else { 
                return "true";
            }
        } else { // Check if account does not exists with that name, if so FALSE
            if (tempAccToCardFileName.containsKey(encAcc)) {
                return "true";
            } else {
                return "false";
            }
            
        }
    }
    private <K,V> Map<K,V> deepCopy (Map<K,V> original) {
        Set<Map.Entry<K,V>> origEntries = original.entrySet();
        Map<K,V> copy = new HashMap<K,V>();
        for (Map.Entry<K,V> entry : origEntries) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }
    /**
     * Replace permanent data structures with deep copies of temps
     */
    private void finalizeTransaction () {
        accToCardFileName = deepCopy(tempAccToCardFileName);
        accToBalance = deepCopy(tempAccToBalance);
        cardFileNameToCardNum = deepCopy(tempCardFileNameToCardNum);
    }
    /**
    * PROCESSING JSON
    */
    protected void processArgument(String action_flag, String account, String action) throws Exception {
        if (action_flag.equals("-n")) {
            createAccount(account, action);
        } else if (action_flag.equals("-d")) {
            makeDeposit(account, action);
        } else if (action_flag.equals("-w")) {
            makeWithdrawl(account, action);
        } else if (action_flag.equals("-g")) {
            getBalance(account);
        } else {
            throw new Exception();
        }
    }
     protected void makeDeposit (String account, String action) {
        // Create JSON
        JsonObject model = Json.createObjectBuilder()
                        .add("account", account)
                        .add("deposit", Double.parseDouble(action))
                
                        
            .build();
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model);
        jsonWriter.close();
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        System.out.flush();// flush STDOUT
    } 
    protected void makeWithdrawl(String account, String action) {
        
        // Create JSON
        JsonObject model = Json.createObjectBuilder()
                    .add("account", account)
                    .add("withdraw", Double.parseDouble(action))
                    
                    
            .build();
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model);
        jsonWriter.close();
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        System.out.flush();// flush STDOUT
    }
    protected void createAccount (String account, String action) {
        
        // Create JSON
        JsonObject model = Json.createObjectBuilder()
                    .add("initial_balance", Double.parseDouble(action))
                    .add("account", account)
                    
            .build();
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model);
        jsonWriter.close();
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        System.out.flush();// flush STDOUT
    }
    protected void getBalance (String account) throws Exception {
        /* encrypt account value */
        String encAcc = encryptMessage(account, DBencryptionKey);
        String balance = accToBalance.get(encAcc).toString();
        // Create JSON
        JsonObject model = Json.createObjectBuilder()
                    .add("account", account)
                    .add("balance", Double.parseDouble(balance))
                    
                    
            .build();
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model);
        jsonWriter.close();
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        System.out.flush();// flush STDOUT
    }
}