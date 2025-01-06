package com.CBHub.wrapper.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5Hasher
{

    public static String getMd5(String input) throws NoSuchAlgorithmException {
        MessageDigest md =MessageDigest.getInstance("MD5");

        //digest called to calc message digest, return array bytes
        byte[] messageDigest = md.digest(input.getBytes());

        //convert to signum rep
        BigInteger no = new BigInteger(1,messageDigest);

        //convert into hex
        String hashedText =  no.toString(16);
        while (hashedText.length() < 32) {
            hashedText = "0" + hashedText;
        }
return hashedText;
    }
}
