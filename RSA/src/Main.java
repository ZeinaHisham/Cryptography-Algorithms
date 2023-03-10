import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		String plaintext = "zeina";
		
		int[] textDec = new int[plaintext.length()];
		for(int i=0; i<plaintext.length(); i++) {
			char x = plaintext.charAt(i);
			textDec[i] = (int)x;
		}
		
		int keys[] = KeyGen.generateKeys();
		ArrayList<Long> encrypted = RSA.encrypt(textDec, keys[3], keys[2]);
		String decrypted = RSA.decrypt(encrypted, keys[0], keys[1], keys[4], keys[2]);
		
		System.out.println("p: " + keys[0] + ", q: " + keys[1] + ", n: " + keys[2]);
		System.out.println("e: " + keys[3] + ", d: " + keys[4]);
		System.out.println("Ciphertext: " + encrypted);
		System.out.println("Decrypted Text: " + decrypted);
	}

}
