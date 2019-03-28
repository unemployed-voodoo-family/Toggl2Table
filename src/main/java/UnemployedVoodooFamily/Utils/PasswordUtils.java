package UnemployedVoodooFamily.Utils;

import java.util.Base64;


public class PasswordUtils {


    public static String decodeSecurePassword(String securePassword) {
        String returnvalue = "";
        if(securePassword != null) {
            byte[] passwordAsBytes = Base64.getDecoder().decode(securePassword);
            returnvalue = new String(passwordAsBytes);
        }
        return returnvalue;
    }

    public static String generateSecurePassword(String password) {
        String returnValue;
        byte[] passwordAsBytes = password.getBytes();

        returnValue = Base64.getEncoder().encodeToString(passwordAsBytes);

        return returnValue;
    }

}