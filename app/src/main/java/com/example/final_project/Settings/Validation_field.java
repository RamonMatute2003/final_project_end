package com.example.final_project.Settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation_field{
    private static final String EMAIL_PATTERN="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_PATTERN = "^[0-9]{8,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{8,}$";
    private static final String NAME_PATTERN = "^[\\p{L}]+(\\s[\\p{L}]+)*$";
    private static final String DNI_PATTERN = "^[0-9]{13}$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidName(String name) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidDni(String dni) {
        Pattern pattern = Pattern.compile(DNI_PATTERN);
        Matcher matcher = pattern.matcher(dni);
        return matcher.matches();
    }

    public static boolean isValidBirthdate(String date){
        int year=Integer.parseInt(date.substring(0,4));
        if(year>2010){
            return false;
        }else{
            return true;
        }
    }
}
