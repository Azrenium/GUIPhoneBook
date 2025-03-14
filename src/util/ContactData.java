package util;

import java.util.regex.Pattern;

public class ContactData {
    private static Pattern emailRegexPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public static boolean isValid(String name, String phone, String email, String address) {
        //Name
        if(name.isEmpty() || name.length() > 20) return false;

        //Phone
        if(phone.length() != 10) return false;
        for(char c : phone.toCharArray()) if(!Character.isDigit(c)) return false;

        //Email
        if(email.isEmpty() || email.length() > 40) return false;
        if(!emailRegexPattern.matcher(email).matches()) return false;

        //Address
        if(address.isEmpty() || address.length() > 50) return false;

        return true;
    }
}
