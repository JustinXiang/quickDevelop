package com.zhenxinkeji.poly_agent.net.encryption;


import android.util.Base64;

import com.zhenxinkeji.poly_agent.utils.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesUtil {

    // 使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！
    public static String CARD_KEY = "N5xcCUzUD6Tl5yFE";

    public static String POLY_KEY = "3E198ABA433445A1";

    private static String IV  = "3E198ABA433445A1";

    /**
     * ecb加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String ecbEncrypt(String data) throws Exception{
        byte[] dataBytes = data.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(POLY_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] bytes = cipher.doFinal(dataBytes);
        //转成16进制
        String ecb = ArrayUtils.parseByte2HexStr(bytes).toLowerCase();
        return ecb;
    }

    /**
     * ecb解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String ecbDecrypt(String data) throws Exception{
        //转成二进制
        byte[] dataBytes = ArrayUtils.parseHexStr2Byte(data);
        SecretKeySpec skeySpec = new SecretKeySpec(POLY_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decryptData = cipher.doFinal(dataBytes);
        return new String(decryptData);
    }

    /**
     * 加密方法
     *
     * @param data 要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");// "算法/模式/补码方式"NoPadding PkcsPadding
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    /**
     * 解密方法
     *
     * @param data 要解密的数据
     * @param key 解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted1 = Base64.decode(data, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    /**
     * 使用默认的key和iv加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        return encrypt(data, key, IV).trim();
    }

    /**
     * 使用默认的key和iv解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data, String key) throws Exception {
        return desEncrypt(data, key, IV).trim();
    }

    /**
     * 测试
     */
    public static void main(String[] args) throws Exception {
        String test1 = "{\"loginName\":\"root\",\"loginPwd\":\"123456\"}";
        String test = new String(test1.getBytes(), "UTF-8");
        String data = null;
        String key = "N5xcCUzUD6Tl5yFE";
        String iv = IV;
        // /g2wzfqvMOeazgtsUVbq1kmJawROa6mcRAzwG1/GeJ4=
        data = ecbEncrypt(test);
        System.out.println("数据：" + test);
        System.out.println("加密：" + data);
        String jiemi = ecbDecrypt(data).trim();
        System.out.println("解密：" + jiemi);
    }


}
