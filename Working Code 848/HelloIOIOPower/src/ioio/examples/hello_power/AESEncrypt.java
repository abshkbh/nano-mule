package ioio.examples.hello_power;

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

public class AESEncrypt {

	byte[] preSharedKey128 ={0x06,(byte)0xa9,0x21,0x40,0x36,(byte)0xb8,(byte)0xa1,0x5b,0x51,0x2e,0x03,(byte)0xd5,0x34,0x12,0x00,0x06};
	byte[] iv ={0x3d, (byte) 0xaf ,(byte)0xba,0x42,(byte) 0x9d,(byte)0x9e,(byte)0xb4,0x30,(byte)0xb4,0x22,(byte)0xda,(byte)0x80,0x2c,(byte)0x9f,(byte)0xac,0x41};
	
	/**
   * Turns array of bytes into string
   *
   * @param buf	Array of bytes to convert to hex string
   * @return	Generated hex string
   */
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

public byte[] encrypt(String inputString)
{
	

	byte[] preSharedKey = preSharedKey128;
	
	IvParameterSpec ips = new IvParameterSpec(iv);

	byte[] encodedKey = new byte[preSharedKey.length];
	System.arraycopy(preSharedKey, 0, encodedKey, 0, preSharedKey.length);
	SecretKey aesKey = new SecretKeySpec(encodedKey, "AES");
	Cipher encryptCipher = null;
	
	try {
		
		encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// Initialize the Cipher with key and parameters
	try {
		encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, ips);
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidAlgorithmParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	// Our cleartext
	
	byte[] cleartext = inputString.getBytes();

	// Encrypt the cleartext
	byte[] ciphertext=null;
	try {
		ciphertext = encryptCipher.doFinal(cleartext);
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block.
		e.printStackTrace();
	}
	
//System.out.println(ciphertext.toString());
return ciphertext;




}



}
