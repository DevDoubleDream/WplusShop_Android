package kr.wdream.wplusshop.common.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by deobeuldeulim on 2016. 11. 28..
 */

public class AESUtil {

    public static String[] arrayKey = {"fe8913247de7cd71", "fe1234248de7cd71", "fe1234248de7cf71", "fe1231278de7cd71"};

    public static byte[] hexToByteArray(String hex){

        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];

        for(int i=0; i<ba.length; i++){
            ba[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }

        return ba;
    }

    public static String byteArrayToHex(byte[] ba){

        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;

        for(int x=0; x<ba.length; x++){
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }


    public static String encrypt(String message){
        int rnd = new Random().nextInt(arrayKey.length);

        SecretKeySpec skeySpec = new SecretKeySpec(arrayKey[rnd].getBytes(), "AES");

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] encrypted = cipher.doFinal(message.getBytes());

            return byteArrayToHex(encrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted){
        String originalString = "";

        for(int i=0; i<arrayKey.length; i++){
            SecretKeySpec skeySpec = new SecretKeySpec(arrayKey[i].getBytes(), "AES");

            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);

                byte[] original = cipher.doFinal(hexToByteArray(encrypted));
                originalString = new String(original);

                if(originalString.length() != 0){
                    break;
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                continue;
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
                continue;
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                continue;
            } catch (BadPaddingException e) {
                e.printStackTrace();
                continue;
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
                continue;
            }
        }
        return originalString;
    }
}
