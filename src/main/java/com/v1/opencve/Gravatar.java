package com.v1.opencve;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Gravatar {
    public static String url;

    static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static void setURL(String email, Integer size){
        String hash = Gravatar.md5Hex(email);
        url="https://secure.gravatar.com/avatar/"+hash+"?s="+size+"&d=retro&r=g";
    }

    public static String getURL(){
        return url;
    }
}