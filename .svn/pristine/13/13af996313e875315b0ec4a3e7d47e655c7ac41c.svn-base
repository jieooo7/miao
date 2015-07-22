package com.liuzhuni.lzn.sec;


/**
 * Created by Andrew Lee on 2015/5/28.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-28
 * Time: 16:18
 */
public class RsaEncode {

    private static String pub="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDvQHkAf0Rmj50jJ8CvOxbS6afCK/WkPCaFwUJPXKZ9CU0P5Rqoqm/qevXxhaer4WZxEvPeJKDqLLQFBAzXRQkYtnNNvUPpksFLgZ031WRKTPrtCTH/7kLQ3iTmzQpc94byCbasobCR0BuP2G/HfmIObXqnG9Z7ODvzJFaUg50kQIDAQAB";


    public static String encode(String plain) throws Exception {

        byte[] cipherData= RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(pub), plain.getBytes());

        return Base64.encode(cipherData);
    }
}
