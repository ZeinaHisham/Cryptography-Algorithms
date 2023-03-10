import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan= new Scanner(System.in); 
		
		String plaintext = "I’m very pleased that the authors have succeeded in creating a highly valuable introduction to"
				+ " the subject of applied cryptography. I hope that it can serve as a guide for practitioners to build"
				+ "more secure systems based on cryptography, and as a stepping stone for future researchers to"
				+ "explore the exciting world of cryptography and its applications";

		plaintext = "abcdefgh";
		
		ArrayList<String> textArray = new ArrayList<String>();
		if(plaintext.length()%8 != 0)
		{
			String specialchar = "!@#$%^&*";
			Random rand = new Random();
			
			int div = plaintext.length()/8 + 1;
			int diff = 8*div - plaintext.length();
			for(int i=0; i<diff; i++)
			{
				plaintext+= specialchar.charAt(rand.nextInt(8));
			}
		}
		
		if(plaintext.length()%8==0) {
			int itr = plaintext.length()/8;
			String []split = plaintext.split("(?<=\\G.{" + 8 + "})");
			
			for(int i=0; i<itr; i++)
				textArray.add(split[i]);
		}
		
		System.out.println("Enter the Key: ");
		String key = scan.nextLine();
		while(key.length()!=8) {
			System.out.println("Key is not 8 characters. Enter another key: ");
			key = scan.nextLine();
		}
		
		ArrayList<String> roundKeysEncryption = KeySchedule.generateEncryptionKeys(key);
		ArrayList<String> encryptedText = Encryption.encryptText(textArray, roundKeysEncryption);
		
		
		ArrayList<String> roundKeysDecryption = KeySchedule.generateDecryptionKeys(key);
		String decryptedText = Decryption.decryptText(encryptedText, roundKeysDecryption);
		
		System.out.println("Encrypted Text: " + encryptedText);
		System.out.println("Decrypted Text: " + decryptedText);
		
		//System.out.println("Encryption keys: " + roundKeysEncryption);
		//System.out.println("Decryption keys: " + roundKeysDecryption);
	}

}
