package org.singledog.monkey.comm;

/**
 * Created by Adam.Wu on 2016/6/28.
 */
public class MD5 {

    public static String md5Encrypt(String str) {
        try {
            java.security.MessageDigest alga = java.security.MessageDigest.getInstance("MD5");
            alga.update(str.getBytes());
            byte[] digesta = alga.digest();
            return (byte2hex(digesta));
        } catch (Exception ex) {
            throw new RuntimeException("encrypt to md5 error!", ex);
        }
    }

    public static String byte2hex(byte[] bytes) {
        StringBuffer hs = new StringBuffer(bytes.length*2);
        for (byte b : bytes) {
            String tmp = Integer.toHexString(b & 0XFF);
            if (tmp.length() == 1)
                tmp = "0" + tmp;
            hs.append(tmp);
        }

        return hs.toString().toLowerCase();
    }

}
