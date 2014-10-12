package com.chongwu.utils.common.MD5_AES;

import java.net.URLEncoder;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Data
{
  private static final String CHAT_SET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static byte[] SHA1(String text) throws Throwable {
    byte[] data = text.getBytes("utf-8");
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.update(data);
    return md.digest();
  }

  public static byte[] AES128Encode(String key, String text)
    throws Throwable
  {
    byte[] keyBytes = key.getBytes("UTF-8");
    byte[] keyBytes16 = new byte[16];
    System.arraycopy(keyBytes, 0, keyBytes16, 0, Math.min(keyBytes.length, 16));

    byte[] data = text.getBytes("UTF-8");
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes16, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
    cipher.init(1, keySpec);

    byte[] cipherText = new byte[cipher.getOutputSize(data.length)];
    int ctLength = cipher.update(data, 0, data.length, cipherText, 0);
    cipher.doFinal(cipherText, ctLength);

    return cipherText;
  }

  public static byte[] AES128Encode(byte[] key, String text)
    throws Throwable
  {
    byte[] data = text.getBytes("UTF-8");
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
    cipher.init(1, keySpec);

    byte[] cipherText = new byte[cipher.getOutputSize(data.length)];
    int ctLength = cipher.update(data, 0, data.length, cipherText, 0);
    cipher.doFinal(cipherText, ctLength);

    return cipherText;
  }

  public static String AES128Decode(String key, byte[] cipherText) throws Throwable
  {
    byte[] keyBytes = key.getBytes("UTF-8");
    byte[] plainText = AES128Decode(keyBytes, cipherText);
    return new String(plainText, "UTF-8");
  }

  public static byte[] AES128Decode(byte[] keyBytes, byte[] cipherText) throws Throwable {
    byte[] keyBytes16 = new byte[16];
    System.arraycopy(keyBytes, 0, keyBytes16, 0, Math.min(keyBytes.length, 16));

    SecretKeySpec keySpec = new SecretKeySpec(keyBytes16, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding", "BC");
    cipher.init(2, keySpec);

    byte[] plainText = new byte[cipher.getOutputSize(cipherText.length)];
    int ptLength = cipher.update(cipherText, 0, cipherText.length, plainText, 0);
    ptLength += cipher.doFinal(plainText, ptLength);

    return plainText;
  }

  public static String byteToHex(byte[] data) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      buffer.append(String.format("%02x", new Object[] { Byte.valueOf(data[i]) }));
    }
    return buffer.toString();
  }

  public static String base62(long value) {
    String result = value == 0L ? "0" : "";
    while (value > 0L) {
      int v = (int)(value % 62L);
      value /= 62L;
      result = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(v) + result;
    }
    return result;
  }

  public static String MD5(String data) {
    if (data == null) {
      return null;
    }

    byte[] tmp = rawMD5(data);
    if (tmp == null) {
      return null;
    }

    return HEX.toHex(tmp);
  }

  public static byte[] rawMD5(String data) {
    if (data == null) {
      return null;
    }

    byte[] md5 = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(data.getBytes("utf-8"));
      md5 = md.digest();
    } catch (Exception e) {
      e.printStackTrace();
      md5 = null;
    }
    return md5;
  }

  public static String urlEncode(String s, String enc) throws Throwable {
    String text = URLEncoder.encode(s, enc);
    return text.replace("\\+", "%20");
  }
}