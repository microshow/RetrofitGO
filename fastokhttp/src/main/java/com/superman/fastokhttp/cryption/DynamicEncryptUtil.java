package com.superman.fastokhttp.cryption;


import android.annotation.SuppressLint;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DynamicEncryptUtil {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	@SuppressLint("TrulyRandom") 
	private static byte[] encrypt(byte[] iv, byte[] data,int sortNum) throws Exception {
		byte[] pass = findkeyBySortNum(sortNum);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec dps = new IvParameterSpec(iv);
		SecretKeySpec skeySpec = new SecretKeySpec(pass, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, dps);
		byte[] buf = cipher.doFinal(data);
		return buf;
	}

	private static byte[] decrypt(byte[] iv, byte[] data,int sortNum) throws Exception {
		byte[] pass = findkeyBySortNum(sortNum);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec dps = new IvParameterSpec(iv);
		SecretKeySpec skeySpec = new SecretKeySpec(pass, "AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, dps);
		byte[] buf = cipher.doFinal(data);
		return buf;
	}

	/**
	 * @Description: 获取随机字节
	 * @param
	 * @return byte[]
	 */
	private static byte[] getRamdomBytes(int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = (byte) (Math.round(Math.round(Math.random() * 255)) - 128);
		}
		return result;
	}
//	private static final byte[] PASSKEY0 =new byte[]{0x6D, 0x6E, 0x66, 0x61, 0x6E, 0x66, 0x6F, 0x73, 0x61, 0x6A, 0x66, 0x6F, 0x69, 0x73, 0x6A, 0x76 };  
//	private static final byte[] PASSKEY1 =new byte[]{0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75 };  
//	private static final byte[] PASSKEY2 =new byte[]{0x73, 0x65, 0x72, 0x72, 0x66, 0x73, 0x63, 0x66, 0x73, 0x66, 0x73, 0x61, 0x66, 0x61, 0x73, 0x66 };  
//	private static final byte[] PASSKEY3 =new byte[]{0x72, 0x65, 0x66, 0x64, 0x63, 0x72, 0x73, 0x64, 0x66, 0x65, 0x61, 0x74, 0x76, 0x65, 0x72, 0x65 };  
//	private static final byte[] PASSKEY4 =new byte[]{0x65, 0x76, 0x62, 0x68, 0x79, 0x73, 0x61, 0x63, 0x68, 0x72, 0x79, 0x62, 0x73, 0x61, 0x78, 0x7A };  
//	private static final byte[] PASSKEY5 =new byte[]{0x65, 0x63, 0x72, 0x61, 0x73, 0x72, 0x66, 0x64, 0x73, 0x66, 0x68, 0x76, 0x61, 0x78, 0x6F, 0x65 };  
//	private static final byte[] PASSKEY6 =new byte[]{0x6B, 0x6F, 0x6E, 0x6C, 0x6A, 0x69, 0x6A, 0x6E, 0x6F, 0x6A, 0x69, 0x6F, 0x6A, 0x64, 0x61, 0x73 };  
//	private static final byte[] PASSKEY7 =new byte[]{0x61, 0x72, 0x65, 0x63, 0x75, 0x6B, 0x6E, 0x73, 0x72, 0x65, 0x72, 0x65, 0x72, 0x64, 0x7A, 0x6C };  
//	private static final byte[] PASSKEY8 =new byte[]{0x6C, 0x6D, 0x69, 0x6A, 0x6D, 0x6E, 0x73, 0x66, 0x73, 0x66, 0x64, 0x66, 0x61, 0x6E, 0x69, 0x6F };  
//	private static final byte[] PASSKEY9 =new byte[]{0x6E, 0x6C, 0x6A, 0x69, 0x6F, 0x6A, 0x61, 0x69, 0x66, 0x6F, 0x73, 0x69, 0x61, 0x6A, 0x71, 0x6D }; 
	
	private static final byte[] PASSKEY0 =new byte[]{0x72, 0x77, 0x67, 0x44
		, 0x69, 0x71, 0x4D, 0x33, 
		0x32, 0x76, 0x35, 0x40, 
		0x39, 0x43, 0x52, 0x21 };  
	private static final byte[] PASSKEY1 =new byte[]{0x54,0x62,0x43,0x59,0x24,0x51,0x47,0x6d,0x73,0x25,0x70,0x5a,0x36,0x26,0x63,0x72};  
	private static final byte[] PASSKEY2 =new byte[]{0x65,0x77,0x38,0x4e,0x37,0x50,0x6a,0x57,0x75,0x6b,0x40,0x31,0x21,0x44,0x33,0x56};  
	private static final byte[] PASSKEY3 =new byte[]{0x39,0x6d,0x37,0x4a,0x57,0x61,0x50,0x48,0x2a,0x70,0x6e,0x49,0x38,0x42,0x6a,0x24};  
	private static final byte[] PASSKEY4 =new byte[]{0x25,0x52,0x43,0x46,0x73,0x57,0x63,0x42,0x32,0x64,0x41,0x7a,0x47,0x55,0x48,0x79 };  
	
	private static final byte[] PASSKEY5 =new byte[]{0x48,0x73,0x64,0x38,0x34,0x77,0x50,0x7a,0x35,0x65,0x71,0x70,0x67,0x40,0x58,0x6a };  
	private static final byte[] PASSKEY6 =new byte[]{0x44,0x62,0x21,0x24,0x35,0x68,0x41,0x53,0x30,0x4e,0x33,0x39,0x5a,0x55,0x49,0x50 };  
	private static final byte[] PASSKEY7 =new byte[]{0x77,0x33,0x38,0x41,0x45,0x35,0x59,0x2a,0x64,0x4e,0x6b,0x55,0x4f,0x69,0x57,0x21 };  
	private static final byte[] PASSKEY8 =new byte[]{0x4a,0x63,0x70,0x57,0x46,0x45,0x79,0x31,0x4c,0x35,0x65,0x49,0x34,0x4f,0x26,0x67 };  
	private static final byte[] PASSKEY9 =new byte[]{0x43,0x39,0x69,0x6a,0x6e,0x48,0x75,0x4c,0x44,0x65,0x64,0x21,0x33,0x37,0x26,0x56 }; 
	
	public static byte[] findkeyBySortNum(int sortNum){
		byte[] randKey=null;
		switch (sortNum) {
		case 0:
			randKey=PASSKEY0;
			break;
		case 1:
			randKey=PASSKEY1;
			break;
		case 2:
			randKey=PASSKEY2;
			break;
		case 3:
			randKey=PASSKEY3;
			break;
		case 4:
			randKey=PASSKEY4;
			break;
		case 5:
			randKey=PASSKEY5;
			break;
		case 6:
			randKey=PASSKEY6;
			break;
		case 7:
			randKey=PASSKEY7;
			break;
		case 8:
			randKey=PASSKEY8;
			break;
		case 9:
			randKey=PASSKEY9;
			break;
		default:
			randKey= PASSKEY9;
			break;
	}
		return randKey;
		
	}
	
	 public static int createRandomkeySort(){
		   int randomNum=(int)(Math.random()*10);
		   return randomNum;
	   }
	 /**
	  * 加密
	  * @param data
	  * @return
	  */
	 public static byte[] encrypt(byte[] data) {
		 int sortNum=createRandomkeySort();
		 try {
				byte[] iv = getRamdomBytes(16);
				byte[] buf = encrypt(iv, data,sortNum);
				byte[] result = new byte[iv.length + buf.length+1];
				result[0]=(byte)sortNum;
				System.arraycopy(iv, 0, result, 1, iv.length);
				System.arraycopy(buf, 0, result, iv.length+1, buf.length);
				return result;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	 /**
	  * 解密
	  * @param srcData
	  * @return
	  */
		public static String decrypt(byte[] srcData) {
			 int sortNum=srcData[0];
			 byte[] data=new byte[srcData.length-1];
		     System.arraycopy(srcData, 1, data, 0, srcData.length-1);
			try {
				byte[] iv = new byte[16];
				System.arraycopy(data, 0, iv, 0, 16);
				byte[] src = new byte[data.length - iv.length];
				System.arraycopy(data, 16, src, 0, data.length - iv.length);
				byte[] tmp = decrypt(iv, src,sortNum);
				return new String(tmp, "utf-8");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
		
	public static void main(String[] args) throws Exception {
		String s = "123";
		System.out.println("未加密"+s);
		System.out.println("加密"+encrypt(s.getBytes()));
		byte[] data = encrypt(s.getBytes());
		System.out.println("解密"+decrypt(data));
	}
}
