package com.dstsystems.bps.cipher;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.dstsystems.bps.cipher.exception.keyPhraseException;

import jodd.util.Base64;



public class AESCipher {
	//
	private static byte[] DefaultKeyPhrase = { 0x41, 0x45, 0x53, 0x5f, 0x43,
			0x72, 0x79, 0x70, 0x74, 0x5f, 0x4b, 0x65, 0x79, 0x20, 0x20, 0x20 };

	public AESCipher() throws keyPhraseException {
		_key = generateKey(DefaultKeyPhrase);
	}

	public AESCipher(String keyPhrase) throws keyPhraseException {
		_key = generateKey(keyPhrase);
	}

	public byte[] encrypt(byte[] messageData) {

		Cipher cipher = null;
		byte[] encyrptData = null;
		try {
			cipher = Cipher.getInstance(CipherContants.ALGO_PAD);
			cipher.init(Cipher.ENCRYPT_MODE, _key);
			encyrptData = cipher.doFinal(messageData);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | BadPaddingException
				| IllegalBlockSizeException e) {

			System.out.println(e.getMessage());
			encyrptData = null;
		}
		return encyrptData;
	}

	public String encyrpt(String message) {
		byte[] encrptData = encrypt(message.getBytes());
		// BASE64Encoder encode = new BASE64Encoder();
		// final String encryptMsg = encode.encode(encrptData);
		final String encryptMsg = Base64.encodeToString(encrptData);

		return encryptMsg;
	}

	public String decrypt(String message) throws IOException {
		// BASE64Decoder decode = new BASE64Decoder();

		// byte[] encryptData = decode.decodeBuffer(message);
		byte[] encryptData = Base64.decode(message);
		byte[] clearData = decrypt(encryptData);
		return new String(clearData);
	}

	public byte[] decrypt(byte[] messageData) {

		Cipher cipher = null;
		byte[] decyrptData = null;
		try {
			cipher = Cipher.getInstance(CipherContants.ALGO_PAD);
			cipher.init(Cipher.DECRYPT_MODE, _key);
			decyrptData = cipher.doFinal(messageData);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {

			System.out.println(e.getMessage());
			decyrptData = null;
		}

		return decyrptData;
	}

	private Key generateKey(String keyPhrase) throws keyPhraseException {

		ValidateKeyLen(keyPhrase);

		keyPhrase = padRight(keyPhrase, CipherContants.KEY_LEN);

		char[] chars = keyPhrase.toCharArray();
		byte[] bytePhrase = new byte[CipherContants.KEY_LEN];
		for (int i = 0, n = chars.length; i < n; i++) {
			bytePhrase[i] = Byte.parseByte(Integer.toHexString(chars[i]), 16);
		}

		return generateKey(bytePhrase);
	}

	private Key generateKey(byte[] keyPhrase) {
		Key key = new SecretKeySpec(keyPhrase, CipherContants.ALGO);
		return key;
	}

	private String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	private void ValidateKeyLen(String key) throws keyPhraseException {
		if (key.length() > CipherContants.KEY_LEN) {
			throw new keyPhraseException("KeyPhrase must be less than "
					+ Integer.toString(CipherContants.KEY_LEN) + " bytes");
		}
	}

	@SuppressWarnings("unused")
	private String createKey(String key, Boolean pad) throws keyPhraseException {

		ValidateKeyLen(key);

		key = padRight(key, CipherContants.KEY_LEN);

		StringBuilder buf = new StringBuilder(key.length() * 4);
		for (char ch : key.toCharArray()) {
			if (buf.length() > 0 && pad) {
				buf.append(' ');
				buf.append(String.format(",0x%02x", (int) ch));
			} else {
				buf.append(String.format("0x%02x", (int) ch));
			}
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		AESCipher cipher = null;
		
		if(args.length <2 || args.length >3  ){
			System.out.println("Ussage: AESCipher <-e> <String to encrypt> [Key Phrase]");
			System.out.println("Ussage: AESCipher <-d> <String to decrypt> [Key Phrase]");
			return;
		}
		
		try {
		
			if(args.length == 2){
				cipher = new AESCipher();
			}
			else
			{
				cipher = new AESCipher(args[2]);
			}	
			
			if(args[0].equals("-e")){
				System.out.println(cipher.encyrpt(args[1]));
			}
			else{
				System.out.println(cipher.decrypt(args[1]));
			}
		} catch (keyPhraseException | IOException e) {
			System.out.println(e.getMessage());
		}
		finally{
			cipher = null;
		}
	}

	private Key _key;
}
