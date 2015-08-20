package com.samuelnotes.commonlibs.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.text.TextUtils;
import android.util.Base64;

public class DesEncryptFinal {
	
	/**
	 *  DES加密的私钥，必须是8位长的数字，
	 */
	private static final String DESkey = "82021572";// 设置密钥

	/**
	 * 设置密钥编码向量 八位数字，12345678 
	 */
	private static final String ivStr = "12345678";
	

	static AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现

	private static Key key = null;

	public DesEncryptFinal() throws Exception {
		DESKeySpec keySpec = new DESKeySpec(getDESkey());// 设置密钥参数
		iv = new IvParameterSpec(getDESIV());// 设置向量
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		key = keyFactory.generateSecret(keySpec);// 得到密钥对象

	}

	private byte[] getDESIV() {
		if(TextUtils.isEmpty(ivStr)){
			throw new RuntimeException("illegal iv params .");
		}
		return ivStr.getBytes();
	}
	
	private byte[] getDESkey() {
		if(TextUtils.isEmpty(DESkey)){
			throw new RuntimeException("DES key  is error format  .");
		}
		return DESkey.getBytes();
	}
	
	
	public String encode(String data) throws Exception {
		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
		enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
		byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
		// BASE64Encoder base64Encoder = new BASE64Encoder();
		return Base64.encodeToString(pasByte, Base64.NO_WRAP).toString();
	}

	public String decode(String data) throws Exception {
		Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, key, iv);

		// BASE64Decoder base64Decoder = new BASE64Decoder();

		byte[] pasByte = deCipher.doFinal(Base64.decode(data, Base64.DEFAULT));
		// byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
		return new String(pasByte, "UTF-8");
	}

	
	
}
