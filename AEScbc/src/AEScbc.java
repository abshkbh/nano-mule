import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;


public class AEScbc {

	
	  /**
     * Turns array of bytes into string
     *
     * @param buf	Array of bytes to convert to hex string
     * @return	Generated hex string
     */
     public static String asHex (byte buf[]) {
      StringBuffer strbuf = new StringBuffer(buf.length * 2);
      int i;

      for (i = 0; i < buf.length; i++) {
       if (((int) buf[i] & 0xff) < 0x10)
	    strbuf.append("0");

       strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
      }

      return strbuf.toString();
     }


public static void main(String[] args)
{
	
	byte[] loginId = "login".getBytes();

	byte[] preSharedKey128 ={0x06,(byte)0xa9,0x21,0x40,0x36,(byte)0xb8,(byte)0xa1,0x5b,0x51,0x2e,0x03,(byte)0xd5,0x34,0x12,0x00,0x06};
	
	//byte[] preSharedKey128 = "ACME-1234AC".getBytes();
	byte[] preSharedKey192 = "ACME-1234ACME-1234A".getBytes();
	// 256 bit key
	byte[] preSharedKey256 = "ACME-1234ACME-1234ACME-1234".getBytes();
	byte[] preSharedKey = preSharedKey128;

	// Initialization Vector
	// Required for CBC
	//byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
	
	byte[] iv ={0x3d, (byte) 0xaf ,(byte)0xba,0x42,(byte) 0x9d,(byte)0x9e,(byte)0xb4,0x30,(byte)0xb4,0x22,(byte)0xda,(byte)0x80,0x2c,(byte)0x9f,(byte)0xac,0x41};
	IvParameterSpec ips = new IvParameterSpec(iv);

//	byte[] encodedKey = new byte[loginId.length + preSharedKey.length];

	byte[] encodedKey = new byte[preSharedKey.length];
//	System.arraycopy(loginId, 0, encodedKey, 0, loginId.length);
//	System.arraycopy(preSharedKey, 0, encodedKey, loginId.length, preSharedKey.length);
	
	System.arraycopy(preSharedKey, 0, encodedKey, 0, preSharedKey.length);
	// The SecretKeySpec provides a mechanism for application-specific generation
	// of cryptography keys for consumption by the Java Crypto classes.

	// Create a key specification first, based on our key input.
	SecretKey aesKey = new SecretKeySpec(encodedKey, "AES");
 
	
	// Create a Cipher for encrypting the data using the key we created.
	Cipher encryptCipher = null;

	try {
		//encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		encryptCipher = Cipher.getInstance("AES/ECB/NoPadding");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// Initialize the Cipher with key and parameters
	try {
		encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 

	// Our cleartext
    String clearString = "Single Block Msg";
	//String clearString = "0000000000000000";
	byte[] cleartext = clearString.getBytes();

	// Encrypt the cleartext
	byte[] ciphertext=null;
	try {
		ciphertext = encryptCipher.doFinal(cleartext);
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	// Now decrypt back again...
	// Decryption cipher
	Cipher decryptCipher = null;
	try {
		decryptCipher = Cipher.getInstance("AES/ECB/NoPadding");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// Initialize PBE Cipher with key and parameters
	try {
		decryptCipher.init(Cipher.DECRYPT_MODE, aesKey);
	} catch (InvalidKeyException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 

	// Decrypt the cleartext
	byte[] deciphertext = null ;
	try {
		deciphertext = decryptCipher.doFinal(ciphertext);
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	System.out.println("encrypted string: " + asHex(ciphertext));
	System.out.println("String in hex   : " + asHex(cleartext));
	System.out.println("decrypted string: " + asHex(deciphertext));
	
	
	
	
}
	



















}
