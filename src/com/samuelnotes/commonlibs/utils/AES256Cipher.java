package com.samuelnotes.commonlibs.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * 主要进行字符串的加解密 对于需要进行加密或者解密的字符串 1.获取公钥：需要调用与服务器端商量定的公钥 2.直接调用加解密方法 调用示例： public
 * void encryptdecrypt(){
 * 
 * String string =
 * " 123456H26019001ABCkfg++--//**'/.,!@#$%^&*()_+";
 * String encryptStr; try { encryptStr = AES256Cipher.AES_Encode(string);
 * L.d(getClass(), ""+ encryptStr ); String decryptStr =
 * AES256Cipher.AES_Decode(encryptStr); L.d(getClass(), ""+ decryptStr ); }
 * catch (Exception e) { e.printStackTrace(); } }
 * 
 * @author H2601901
 */
public class AES256Cipher {

//	static JniManager jniManager;
//	static {
//		jniManager = JniManager.getInstance();
//	}

	public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	private static final String IPORTAL_SECURITY_KEY = "abcdefghijklmnopqrstuvwxyz0123456789";

	/**
	 * 加密 使用系统默认KEY
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String AES_Encode(String str) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		return AES_Encode(str, IPORTAL_SECURITY_KEY);
	}

	/**
	 * 解密 使用系统默认KEY
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String AES_Decode(String str) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		return AES_Decode(str, IPORTAL_SECURITY_KEY);
	}

	/**
	 * 加密
	 * 
	 * @param str
	 *            The string to encrypted
	 * @param key
	 *            The corresponding public key encryption
	 * @return Cipherstring
	 * @throws java.io.UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String AES_Encode(String str, String key)
			throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

		return Base64.encodeToString(cipher.doFinal(textBytes), Base64.NO_WRAP);
	}

	/**
	 * 解密
	 * 
	 * @param str
	 *            The string to decrypted
	 * @param key
	 *            The corresponding public key encryption
	 * @return Plainstring
	 * @throws java.io.UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String AES_Decode(String str, String key)
			throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] textBytes = Base64.decode(str, Base64.NO_WRAP);
		// byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

}