package com.example.dairyinventoryservice.util;

import java.util.Random;



public class GenerateOtpUtil {
    public static int generateOTP() {
        Random random = new Random();
        // Generates a number between 100000 and 999999 (inclusive)
        return 100000 + random.nextInt(900000);
    }
}
