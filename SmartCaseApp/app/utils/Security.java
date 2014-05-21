/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Formatter;
import static play.mvc.Results.internalServerError;

/**
 *
 * @author bombrunt
 */
public class Security {
    private static MessageDigest md = null;
    
    public static String crypt(String s){

        try {
            if(md==null) {
                md = MessageDigest.getInstance("MD5");
            }
            byte[] bytesOfMessage = s.getBytes("UTF-8");
            byte[] thedigest = md.digest(bytesOfMessage);
            return byteToHex(thedigest);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            return "";
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "";
        }
    }
    
    
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    
    private static String byteToHex(final byte[] hash)
    {
        
        char[] chars = new char[2 * hash.length];
        for (int i = 0; i < hash.length; ++i) {
            chars[2 * i] = HEX_CHARS[(hash[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[hash[i] & 0x0F];
        }
        return new String(chars);

    }
    
}
