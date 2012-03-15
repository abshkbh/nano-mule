

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESDecrypt {

	byte[] preSharedKey128 ={0x06,(byte)0xa9,0x21,0x40,0x36,(byte)0xb8,(byte)0xa1,0x5b,0x51,0x2e,0x03,(byte)0xd5,0x34,0x12,0x00,0x06};
	byte[] iv ={0x3d, (byte) 0xaf ,(byte)0xba,0x42,(byte) 0x9d,(byte)0x9e,(byte)0xb4,0x30,(byte)0xb4,0x22,(byte)0xda,(byte)0x80,0x2c,(byte)0x9f,(byte)0xac,0x41};
	byte[] e;
	
	public static byte[] toByte (String encrypted)
	{
		byte[] e = new byte[encrypted.length()/2]; 
		int j=0;
		int i;
	
		for(i=0;i<encrypted.length();i+=2)
		{
			if ((encrypted.charAt(i)>=48)&& (encrypted.charAt(i)<=57))
			{   
				e[j] =   (byte) (((int)(encrypted.charAt(i)-48)&0x000000ff)<<4);
			    
			}
			
			else
			{
			//	System.out.println((byte)encrypted.charAt(i));
				e[j] =   (byte) (((int)(encrypted.charAt(i)-87)&0x000000ff)<<4);
			}
			
			if ((encrypted.charAt(i+1)>=48)&& (encrypted.charAt(i+1)<=57))
			{
				e[j] |= (byte)(((int)(encrypted.charAt(i+1)-48)&0x000000ff));
			    
			}
			
			else
			{
				e[j] |= (byte)(((int)(encrypted.charAt(i+1)-87)&0x000000ff));
			}
		
		        j++;
		
		}
		
		return e;
		
	}
	
	
	
	
	
	
	
	public String asHex (byte buf[]) {
	    StringBuffer strbuf = new StringBuffer(buf.length * 2);
	    int i;

	    for (i = 0; i < buf.length; i++) {
	     if (((int) buf[i] & 0xff) < 0x10)
		    strbuf.append("0");

	     strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
	    }

	    return strbuf.toString();
	   }
	
	
	public byte[] decrypt(byte[] cryptString)
	{
		

		byte[] preSharedKey = preSharedKey128;
		
		IvParameterSpec ips = new IvParameterSpec(iv);

		byte[] encodedKey = new byte[preSharedKey.length];
		System.arraycopy(preSharedKey, 0, encodedKey, 0, preSharedKey.length);
		SecretKey aesKey = new SecretKeySpec(encodedKey, "AES");
		Cipher decryptCipher = null;

		try {
			
			decryptCipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Initialize the Cipher with key and parameters
		try {
			decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ips);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		// Encrypt the cleartext
		byte[] deciphertext=null;
		try {
			deciphertext = decryptCipher.doFinal(cryptString);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	return deciphertext;




	}












}
