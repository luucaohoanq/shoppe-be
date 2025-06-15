package com.lcaohoanq.sp.utils;

public class OtpUtil {

    public static String generateOtp() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

}
