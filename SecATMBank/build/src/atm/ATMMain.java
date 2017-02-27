package src.atm;
import java.util.*;
import java.io.*;
import java.util.regex.*;
/**
 * Created by Troy on 11/30/16.
 */
/**
 * atm is a client program that simulates an ATM by providing a mechanism for customers to interact with their
 * bank accounts stored on the bank server atm allows customers to create new accounts, deposit money, withdraw funds,
 * and check their balances.
 */
public class ATMMain {
    private static final List<String> ARGUMENTS =
            new ArrayList(Arrays.asList(new String[]{"-s", "-i", "-p", "-c", "-a", "-n", "-d", "-w", "-g"}));
    private static final List<String> ACTIONS = new ArrayList(Arrays.asList(new String[]{"-n", "-d", "-w", "-g"}));
    private static boolean verifyArguments(Map<String, String> argMap) {
        int actions = 0;
        int parameters = 0;
        for(String key : argMap.keySet()) {
            if(ACTIONS.contains(key)) {
                actions++;
            } else if(ARGUMENTS.contains(key)) {
                parameters++;
            } 
        }
        if((argMap.containsKey("-a") && parameters >= 1 && parameters <=5) 
                && actions == 1 && (actions+parameters == argMap.size())){
            return true;
        } else {
            return false;
        }   
    }
    // This checks for false flags and returns the value of the action
    private static String getAction (Map<String, String> argMap) {
        if (argMap.containsKey("-d")) {
            return argMap.get("-d");
        } else if (argMap.containsKey("-n")) {
            return argMap.get("-n");
        } else if (argMap.containsKey("-w")) {
            return argMap.get("-w");
        } else if (argMap.containsKey("-g")) {
            return ""; // nothing to return
        }
        return null;
    }
 // Regex for input sanitization
    private static Pattern argIsAtmOrBank = Pattern.compile("^(atm|bank)$");
    private static Pattern argOneDot = Pattern.compile("^(\\.)$");
    private static Pattern argTwoDot = Pattern.compile("^(\\.{2})$");
    private static Pattern argIsValidFile = Pattern.compile("^([a-z]|[0-9]|_|-|\\.){1,255}$");
    private static Pattern argIsValidAccount = Pattern.compile("^([a-z]|[0-9]|_|-|\\.){1,250}$");
    private static Pattern argStartsWithFlag = Pattern.compile("^(-a|-ag|-ga|-g|-s|-i|-p|-c|-n|-d|-w)");
    private static Pattern argIsEasyParse = Pattern.compile("^(-s|-i|-p|-c|-n|-d|-w|-a)");
    private static Pattern argIsG = Pattern.compile("^(-g)$");
    private static Pattern argIsGA = Pattern.compile("^(-ga)$");
    private static Pattern argStartsWithGA = Pattern.compile("^-(ga)");
    private static Pattern argIsValidNumber = Pattern.compile("^(0|[1-9][0-9]*)$");
    private static Pattern argIsValidAmount = Pattern.compile("^(0|[1-9][0-9]*)(\\.)([0-9]{2})$");
    private static Pattern argIsValidIP = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static boolean sanitizeArg(String arg, String lastFlag, Map<String, String> argMap) {
        boolean status = false;
        if(lastFlag == null) {
            return false;
        }
        switch (lastFlag) {
        case "-a":  
            Matcher validAccount = argIsValidAccount.matcher(arg);
            if(validAccount.find() && !argMap.containsKey("-a")) {
                argMap.put("-a", arg);
                status = true;
            } else {
             //   System.err.println("Invalid -a case");
            }
            break;
        case "-ga":
            Matcher validAccountGA = argIsValidAccount.matcher(arg);
            if(validAccountGA.find() && !argMap.containsKey("-a") && !argMap.containsKey("-g")) {
                argMap.put("-a", arg);
                argMap.put("-g", "");
                status = true;
            } else {
                //System.err.println("Invalid -ga case");
            }
            break;
        case "-s":
            Matcher validFileS = argIsValidFile.matcher(arg);
            Matcher oneDotS = argOneDot.matcher(arg);
            Matcher twoDotS = argTwoDot.matcher(arg);
            if(validFileS.find() && !oneDotS.find() && !twoDotS.find() && !argMap.containsKey("-s")) {
                argMap.put("-s", arg);
                status = true;
            } else {
                //System.err.println("Invalid -s case");
            }
            break;
        case "-c":
            Matcher validFileC = argIsValidFile.matcher(arg);
            Matcher oneDotC = argOneDot.matcher(arg);
            Matcher twoDotC = argTwoDot.matcher(arg);
            if(validFileC.find() && !oneDotC.find() && !twoDotC.find() && !argMap.containsKey("-c")) {
                argMap.put("-c", arg);
                status = true;
            } else {
              //  System.err.println("Invalid -c case");
            }
            break;
        case "-p":
            Matcher validNumber = argIsValidNumber.matcher(arg);
            if(validNumber.find() && !argMap.containsKey("-p")) {
                int argNum = Integer.parseInt(arg);
                if(argNum >= 1024 && argNum <= 65535){
                    argMap.put("-p", Integer.toString(argNum));
                    status = true;
                }  else {
                  //  System.err.println("Invalid -p case1");
                }
            } else {
               // System.err.println("Invalid -p case2");
            }
            break;
        case "-i":
            Matcher validIP = argIsValidIP.matcher(arg);
            if(validIP.find() && !argMap.containsKey("-i")){
                argMap.put("-i", arg);
                status = true;
            } else {
              //  System.err.println("Invalid -i case");
            }
            break;
        case "-d":
            Matcher validAmountB = argIsValidAmount.matcher(arg);
            if(validAmountB.find() && !argMap.containsKey("-d")){
                double doubleAmountB = Double.parseDouble(arg);
                if(doubleAmountB <= 4294967295.99) {
                    argMap.put("-d", arg);
                    status = true;
                } else {
                 //   System.err.println("Invalid -d case1");
                }
            } else {
              //  System.err.println("Invalid -d case2");
            }
            break;
        case "-n":
            Matcher validAmountN = argIsValidAmount.matcher(arg);
            if(validAmountN.find() && !argMap.containsKey("-n")){
                double doubleAmountN = Double.parseDouble(arg);
                if(doubleAmountN <= 4294967295.99) {
                    argMap.put("-n", arg);
                    status = true;
                } else {
                  //  System.err.println("Invalid -n case1");
                }
            } else {
                //System.err.println("Invalid -n case2");
            }
            break;
        case "-w":
            Matcher validAmountW = argIsValidAmount.matcher(arg);
            if(validAmountW.find() && !argMap.containsKey("-w")){
                double doubleAmountW = Double.parseDouble(arg);
                if(doubleAmountW <= 4294967295.99) {
                    argMap.put("-w", arg);
                    status = true;
                } else {
                   // System.err.println("Invalid -w case1");
                }
            } else {
                //System.err.println("Invalid -w case2");
            }
            break;  
        case "-g":
            System.err.println("Error: Argument should not follow -g");
            break;
        default: 
            System.err.println("Default Switch Error");
            break;
        }
        return status;
    }
    
    
    
    
    /**
     * MAIN FUCNTION
     */
    public static void main (String [] args) {
        // Any invocation of the atm which does not follow the four enumerated possibilities should exit
        // with return code 255 (printing nothing).
        Map<String, String> argMap = new HashMap<String, String>();
        String lastFlag = null;
        boolean sanitized = true;
        
        // Sanitize arguments and place them in the HashMap argMap
        // System.out.println("Size:" + args.length);
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            arg.replaceAll("\\s+",""); // Remove all spaces 
            Matcher atmOrBank = argIsAtmOrBank.matcher(arg); 
            Matcher startsWithFlag = argStartsWithFlag.matcher(arg);
            Matcher argGA = argIsGA.matcher(arg);
            Matcher argG = argIsG.matcher(arg);
            if (atmOrBank.find()){ // If arg is atm or bank...
            } else if (startsWithFlag.find() && lastFlag == null){ // Else if the argument starts w/ a valid flag...
                if(arg.length() == 2 || (arg.length() == 3 && argGA.find())) { // arg is a normal flag
                    if(argG.find()) {
                        if(!argMap.containsKey("-g")) {
                            argMap.put("-g", "");
                            lastFlag = null;
                        } else {
                            sanitized = false;
                            break;
                        }
                    } else {
                        lastFlag = arg;
                    }
                } else { 
                    Matcher easyParse = argIsEasyParse.matcher(arg);
                    if(easyParse.find()) {
                        String flag = arg.substring(0,2);
                        String argument = arg.substring(2);
                        sanitized = sanitizeArg(argument, flag, argMap);
                        lastFlag = null;
                    } else { // Argument is appended to -ga
                        Matcher argStartGA = argStartsWithGA.matcher(arg);
                        if(argStartGA.find()) {
                            String flag = arg.substring(0,3);
                            String argument = arg.substring(3);
                            sanitized = sanitizeArg(argument, flag, argMap);
                            lastFlag = null;
                        } 
                    }
                }
            } else { // Else arg is not a flag or flag/arg combo, arg is just an argument
                sanitized = sanitizeArg(arg, lastFlag, argMap);
                lastFlag = null;
            }
            if(!sanitized) { break; }
        } // End of sanitation
        
        if (!sanitized || !verifyArguments(argMap)) {
            System.exit(255);
        }
        
        
        String account = argMap.get("-a");
        Integer port = argMap.containsKey("-p") ? Integer.parseInt(argMap.get("-p")) : 3000;
        String ipAddress = argMap.containsKey("-i") ? argMap.get("-i") : "127.0.0.1";
        String cardFileName = argMap.containsKey("-c") ? argMap.get("-c") : account + ".card";
        String authFileName = argMap.containsKey("-s") ? argMap.get("-s") : "bank.auth";
        String action = getAction(argMap);
        File data_d = new File(cardFileName); // Create file with account.card for checking purposes later
        String action_flag;
        
        if (argMap.containsKey("-n")) {
            Float init_balance = Float.parseFloat(argMap.get("-n"));
            action_flag = "-n";
            // Check if card file already exists and if init_balance is acceptable
            if (init_balance < 10.0 || data_d.exists()) {
                System.exit(255);
            }
        } else if (argMap.containsKey("-d")) { 
            Float amount = Float.parseFloat(argMap.get("-d")); 
            action_flag = "-d";
            if (amount <= 0.0) {
                System.exit(255);
            }
        } else if (argMap.containsKey("-w")) { 
            Float amount = Float.parseFloat(argMap.get("-w"));
            
            action_flag = "-w";
            if (amount <= 0.0) {
                System.exit(255);
            }
            
        } else { 
            action_flag = "-g";
        }
        try {
            ATM atm = new ATM (ipAddress, port, account, authFileName, cardFileName, action, action_flag);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(255);
        }
    }
}