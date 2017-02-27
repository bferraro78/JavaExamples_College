package src.atm;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
/**
 * Created by Troy on 11/30/16.
 */
class ATM {
    private final Socket connection;
    private final BufferedReader connectionIn;
    private final BufferedReader stdIn;
    private final PrintWriter connectionOut;
    private final PrintWriter stdOut;
    private final String account;
    private final String action;
    private final String action_flag;
    private final String cardFileName;
    private final String authFileName;
    private String[] steps;
    private String cardNum;
    private String balance;
    private String sessionToken;
    private final SecretKey encryptionKey;
    /* Creates 16 digit credit card number randomly */
    private static String formatCard() {
        Random rand = new Random();
        long l = (long)(rand.nextDouble()*10000000000000000L);
        String ret = String.format("%016d", l);
        return ret;
    }
    
    /* Gets CC # from card file */
    private String getCardNum(String cardFileName) {
        String ret = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(cardFileName));
            
            String sCurrentLine;
            int count = 0;
            
            while ((sCurrentLine = br.readLine()) != null) {
                count ++;
                if (count == 2) {
                    ret = sCurrentLine;
                }
            }
            ret = decryptMessage(ret);
            br.close();
        } catch (FileNotFoundException e) {
            /* Should not reach here for a card file existing check already happened*/
            System.exit(255);
            e.printStackTrace();
        } catch (Exception e) {
            System.exit(255);
            e.printStackTrace();
        }
        
        return ret;
    }
    
    protected ATM (String hostName, Integer portNumber, String account, String authFileName,
                   String cardFileName, String action, String action_flag) throws Exception {
        
        // Need to verify auth file first
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        stdOut = new PrintWriter(System.out, true);
        connection = new Socket(hostName, portNumber);
        connection.setSoTimeout(10 * 1000);
        connectionOut = new PrintWriter(connection.getOutputStream(), true);
        connectionIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
        this.authFileName = authFileName;
        this.account = account;
        this.action = action;
        this.action_flag = action_flag;
        this.cardFileName = cardFileName;
        this.encryptionKey = getEncryptionKey();
    
        /* Execute functionality */
        authfileExist();
        steps = createSteps();
        bankComms();
    }
    private SecretKey getEncryptionKey () throws Exception {
        File authFile = new File(authFileName);
        String keyContents = new String(FileUtils.readFileToByteArray(authFile));
        byte[] keyByteArr = Hex.decodeHex(keyContents.toCharArray());
        SecretKey authKey = new SecretKeySpec(keyByteArr, "AES");
        return authKey;
    }
    
    private String card() {
        /* Gets card number from card, determines if exists, creates card if need be */
        File  f = new File(cardFileName);
        String cardNum = "";
        
        if (action_flag.equals("-n")) {
               try {
                    File data_d = new File(cardFileName);
                    data_d.createNewFile();
                    FileWriter fw = new FileWriter(data_d);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(account);
                    bw.newLine(); // new line
                    cardNum = formatCard();
                    bw.write(encryptMessage(cardNum));
                    bw.close();
                    fw.close();
                } catch (Exception e) {
                    // exit 255
                    e.printStackTrace();
                }
            
        } else {
            if (f.exists()) {
            cardNum = getCardNum(cardFileName);
            } else {
         //       System.out.println("exit 255, card does not exists");
                System.exit(255);
            }
        }
        return cardNum;
    }
    
    /* Creates steps of strings to send to Bank */
    private String[] createSteps() {
        String [] ret = new String[5];
            for (int i = 0; i < 5; i++) {
                if (i == 0) { // Encrypt and send hello, if encryption correct, bank will see hello, and return "true"
                    // Random string added to avoid predictable encrypted init message
                    ret[i] = "init_check" + " " + "hello" + " " + UUID.randomUUID().toString();
                } else if (i == 1) { // Step 1 - check if account exists
                    ret[i] = "step1" + " " + account + " " + action_flag + " " + sessionToken;
                } else if (i == 2) { // Step 2 -  give card and cardNumber and execute action
                    ret[i]  = "step2" + " " + cardFileName + " " + cardNum +  " " + account + " " + action + " " + action_flag + " " + sessionToken;
                } else if (i == 3) { // Step 3 - END Transmission
                    ret[i]  = "end";
                } 
            }
        return ret;
    }
    
    protected void processArgument() {
        if (action_flag.equals("-n")) {
            createAccount();
        } else if (action_flag.equals("-d")) {
            makeDeposit();
        } else if (action_flag.equals("-w")) {
            makeWithdrawl();
        } else if (action_flag.equals("-g")) {
            getBalance();
        } else {
            // SHOULD NEVER GET HERE
            System.exit(255);
        }
    }
    
    private String encryptMessage (String message) throws Exception {
        byte[] messageByteArr = message.getBytes();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        byte[] cipherTextByteArr = cipher.doFinal(messageByteArr);
        return Base64.getEncoder().encodeToString(cipherTextByteArr);
    }
    
    private String decryptMessage (String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, cipher.getParameters());
        byte[] messageByteArray = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(messageByteArray);
    }
    protected void bankComms() throws Exception {
        String line = "";
        int step = 0;
        try {
            for (int i = 0; i < 4; i++) {
                /* Write step to Bank */
                String step_tmp = steps[step];
              //  System.out.println(step_tmp);
                step_tmp = encryptMessage(step_tmp);
                connectionOut.println(step_tmp);
                connectionOut.flush();
                
                /* Read Banks Response */
                String decyrptline = connectionIn.readLine();
                // System.out.println("EncyrptedMessage: " + decyrptline);
                
                line = decryptMessage(decyrptline);
                // System.out.println("DecyrptedMessage: " + line);
                
                String[] splitArray = line.split(" ");
                String tmp = splitArray[0].toString();
                
                if (i == 0) { // returns false if encryption did not work correct on bank side
                    if (tmp.equals("false")) {
                        System.exit(255);
                    } else {
                        sessionToken = splitArray[1];
                    }
                } else if (i == 1) { // Checks if account is existing or not existing depending on action_flag
                   if (tmp.equals("true")) {
                       if (action_flag.equals("-n")) { // -n
                            cardNum = card();
                       } else { // -w, -d, -g
                            cardNum = card();
                       }
                   } else {
                       System.exit(255);
                   }
               } else if (i == 2) {
                   if (tmp.equals("false")) {
                       System.exit(255);
                   } else {
                       balance = tmp;
                   }
                   /* Wrtie JSON as last thing you do before ATM closes */
                   processArgument();
               }
                
               /* Recreate Steps for CardNumber */
               steps = createSteps();
               step++;
            }
            
            connectionOut.flush();
            connection.close();
            
        } catch (SocketTimeoutException e) {
            System.exit(63);
        } catch (IOException e) {
            System.exit(63);
        } catch (InterruptedException e) {
            System.exit(63);
        }
    }
    private void authfileExist() {
        File n = new File(authFileName);
        if (!(n.exists())) {
            System.exit(255);
        }
        
    }
    protected void makeDeposit () {
        // Create JSON
        JsonObject model = Json.createObjectBuilder()
                    .add("account", account)
                    .add("deposit", Double.parseDouble(action)) // parse deposit to float
                   
            .build();
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model);
        jsonWriter.close();
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        System.out.flush();// flush STDOUT
    } 
    protected void makeWithdrawl() {
        // Create JSON
        JsonObject model = Json.createObjectBuilder()
                        .add("account", account)
                        .add("withdraw", Double.parseDouble(action)) // parse withdraw to float
            .build();
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(model);
        jsonWriter.close();
        String jsonData = stWriter.toString();
        System.out.println(jsonData);
        System.out.flush();// flush STDOUT
    }
    protected String createAccount () {
        
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
        System.out.print(jsonData);
        System.out.flush();// flush STDOUT
        return null;
    }
    protected void getBalance () {
        
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