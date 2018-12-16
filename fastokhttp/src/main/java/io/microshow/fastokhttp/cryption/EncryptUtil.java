package io.microshow.fastokhttp.cryption;

import android.annotation.SuppressLint;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * 加解密辅助类
 * 
 * @author zhichao.huang
 *
 */
public class EncryptUtil {

	/**
	 * 加密
	 * 
	 * @param input
	 * @param key
	 * @return
	 */
	@SuppressLint("TrulyRandom") 
	private static String encrypt(String input, int index) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(DynamicEncryptUtil.findkeyBySortNum(index), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
//			crypted = CryptionUtil.encrypt(input.getBytes(), index, PayApp.pay);
		} catch (Exception e) {
		}
		return new String(Base64.encode(crypted));
	}

	/**
	 * 解密
	 * 
	 * @param input
	 * @param key
	 * @return
	 */
	private static String decrypt(String input, int index) {
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(DynamicEncryptUtil.findkeyBySortNum(index), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decode(input));
//			output = CryptionUtil.decrypt(Base64.decode(input), index, PayApp.pay);
			return new String(output, "UTF-8");
		} catch (Exception e) {

		}
		return "";
	}
	
	/**
	 * 生成加密数据
	 * 
	 * @param content
	 * @return
	 */
	public synchronized static String encryptData (String content) {
		
		int index = EncryptUtil.createRandomkeySort();
		String data="";
		data = EncryptUtil.encrypt(content, index);
//		byte[] data1 =data.getBytes();
//		byte[] secur = new byte[data1.length+1];
//		secur[0]=String.valueOf(index).getBytes()[0];//把密钥拼在元数据的第一个字节
//		System.arraycopy(data1, 0, secur, 1, data1.length);
//		Log.i("xxxx", secur.toString());
		return index+data;
	}
	
	/**
	 * 解密数据
	 * 
	 * @param srcContent 原始数据（未解密的数据）
	 * @return
	 */
	public synchronized static String decryptData (String srcContent) {
		
		byte[] result = srcContent.getBytes();
		
		byte u  = -17;
		byte t = -69;
		byte f = -65;
		
		byte[] parms=null;
		if(result[0]==u && result[1]==t && result[2]==f){
			parms= new byte[result.length-3];
			System.arraycopy(result,3, parms, 0, parms.length);
		}
		String test;
		if(null != parms && parms.length>0){
			test = new String(parms);
		}else{
			test = new String (result);
		}
		
		String ecryptString = test.substring(1,test.length());
		byte[] decrypt = ecryptString.getBytes();
		String indexByte=test.substring(0,1);
		
		String decryptString="";
		decryptString = EncryptUtil.decrypt(new String(decrypt), 
				Integer.parseInt(indexByte));
		
		return decryptString;
	}

	/**
	 * 创建加密随机数
	 * @return
	 */
	public static int createRandomkeySort() {
		int randomNum = (int) (Math.random() * 10);
		return randomNum;
	}
	
}
