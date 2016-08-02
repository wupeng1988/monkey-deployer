package org.singledog.monkey.comm;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by Adam.Wu on 2016/6/28.
 */
public class AES {

    private static final String key = MachineIdentifier.getMachineId().substring(0, 16);

    /**
     * 加密
     * @param content
     * @return
     */
    public static String aesEncrypt(String content) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("encrypt error !", e);
        }
    }

    public static String aesDecrypt(String content) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(original, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("decrypt error !", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(key);
        System.out.println(key.length());
        String source = "t/LXmYQ8d2ec/g7NLpvaQqxmCio+NawnOKsXDP7qYsI=";
//        String dest = aesEncrypt(source);
//        System.out.println(dest);
        System.out.println(aesDecrypt(source));
//        System.out.println(source);
    }
}
