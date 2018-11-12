package com.acq;

import java.security.SecureRandom;
import java.util.Random;

public class OtpGenerator {
	public static String generatePassword() {
        String chars = "123456789";

        final int PW_LENGTH = 6;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < PW_LENGTH; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    }
	public static String generatePwd() {
        String chars = "123456789";

        final int PW_LENGTH = 3;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < PW_LENGTH; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    }

public static String RandomNumberGenerator() {
	    String chars = "0123456789";

        final int PW_LENGTH = 15;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < PW_LENGTH; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    
}
}