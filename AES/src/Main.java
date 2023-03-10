import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner scan= new Scanner(System.in); 
		
		String plaintext = "Im very pleased that the authors have succeeded in creating a highly valuable introduction to"
				+ " the subject of applied cryptography. I hope that it can serve as a guide for practitioners to build "
				+ "more secure systems based on cryptography, and as a stepping stone for future researchers to "
				+ "explore the exciting world of cryptography and its applications";

		plaintext = "Friday";
		
		ArrayList<String> textArray = new ArrayList<String>();
		if(plaintext.length()%16 != 0)
		{
			String specialchar = "*";
			Random rand = new Random();
			
			int div = plaintext.length()/16 + 1;
			int diff = 16*div - plaintext.length();
			for(int i=0; i<diff; i++)
			{
				plaintext+= specialchar.charAt(0);
			}
		}
		
		if(plaintext.length()%16==0) {
			int itr = plaintext.length()/16;
			String []split = plaintext.split("(?<=\\G.{" + 16 + "})");
			
			for(int i=0; i<itr; i++)
				textArray.add(split[i]);
		}
		
		System.out.println("Enter the Key: ");
		String key = scan.nextLine();
		
		boolean f = StringOperations.isHexa(key);
		while(key.length()!=32 || !f) {
			System.out.println("Key is not 32 hexa-decimals. Enter another key: ");
			key = scan.nextLine();
			f = StringOperations.isHexa(key);
		}
		
		ArrayList<String> roundKeysEncryption = KeySchedule.generateEncryptionKeys(key);
		ArrayList<String> encryptedText = Encryption.encryptText(textArray, roundKeysEncryption);
		String decryptedText = Decryption.decryptText(encryptedText, roundKeysEncryption);
		
	}

}
