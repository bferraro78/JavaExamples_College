package src.bank;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// PORT 1738
/**
 * Created by Troy on 11/30/16.
 */
public class BankMain {
    private static final List<String> ARGUMENTS = new ArrayList(Arrays.asList(new String[]{"-s", "-p"}));
    private static Pattern argIsAtmOrBank = Pattern.compile("^(atm|bank)$");
    private static Pattern argOneDot = Pattern.compile("^(\\.)$");
    private static Pattern argTwoDot = Pattern.compile("^(\\.{2})$");
    private static Pattern argIsValidFile = Pattern.compile("^([a-z]|[0-9]|_|-|\\.){1,255}$");
    private static Pattern argIsValidNumber = Pattern.compile("^(0|[1-9][0-9]*)$");
    private static Pattern argStartsWithFlag = Pattern.compile("^(-a|-ag|-ga|-g|-s|-i|-p|-c|-n|-d|-w)");
    
    private static boolean sanitizeArg(String arg, String lastFlag, Map<String, String> argMap) {
        boolean status = false;
        if(lastFlag == null) {
            return false;
        }
        switch (lastFlag) {
            case "-s":
                Matcher validFileS = argIsValidFile.matcher(arg);
                Matcher oneDotS = argOneDot.matcher(arg);
                Matcher twoDotS = argTwoDot.matcher(arg);
                if(validFileS.find() && !oneDotS.find() && !twoDotS.find() && !argMap.containsKey("-s")) {
                    argMap.put("-s", arg);
                    status = true;
                } else {
                    // System.out.println("Invalid -s case");
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
                    //     System.out.println("Invalid -p case1");
                    }
                } else {
                  //   System.out.println("Invalid -p case2");
                }
                break;
            default:
                //System.out.println("Invalid Flag");
                status = false;
                break;
        }
        return status;
    }
    public static void main (String [] args) {
        Map<String, String> argMap = new HashMap<String, String>();
        String lastFlag = null;
        boolean sanitized = true;
        // Puts arguments into argMap, makes accessing argument value easier
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            arg.replaceAll("\\s+",""); // Remove all spaces
            Matcher atmOrBank = argIsAtmOrBank.matcher(arg);
            Matcher startsWithFlag = argStartsWithFlag.matcher(arg);
            if (atmOrBank.find()){ // If arg is atm or bank...
            } else if (startsWithFlag.find() && lastFlag == null) { // Else if the argument starts w/ a valid flag...
                if(arg.length() == 2) { // Argument is a normal flag
                    lastFlag = arg;
                } else { // Else argument is a flag/arg combo; need to parse
                    String flag = arg.substring(0,2);
                    String argument = arg.substring(2);
                    sanitized = sanitizeArg(argument, flag, argMap);
                    lastFlag = null;
                }
            } else { // Else arg is not a flag or flag/arg combo, arg is just an argument
                sanitized = sanitizeArg(arg, lastFlag, argMap);
                lastFlag = null;
            }
            if(!sanitized) { break; }
        } // End of sanitation loop
        if (!sanitized) {
            System.exit(255);
        }
        final String hostName = "127.0.0.1";
        final Integer portNumber = argMap.containsKey("-p") ? Integer.parseInt(argMap.get("-p")) : 3000;
        final String authFileName = argMap.containsKey("-s") ? argMap.get("-s") : "bank.auth";
        try {
            Bank bank = new Bank(hostName, portNumber, authFileName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(255);
        }
    }
}