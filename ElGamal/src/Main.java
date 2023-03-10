import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		String plaintext = "abcdefgh";
		
		int[] res = ElGamal.GenerateKeys();
		ArrayList<ArrayList<Integer>> cipher = ElGamal.Encrypt(plaintext, res);
		ArrayList<ArrayList<Integer>> decrypted = ElGamal.Decrypt(cipher.get(0), cipher.get(1), res[3], res[0]);

		System.out.println("KpubB: (p: " + res[0] + ", alpha: " + res[1] + ", beta: " + res[2] + ")");
		System.out.println("Ephermal Key: " + cipher.get(0));
		System.out.print("Ciphertext: ");
		for(int i=0; i<cipher.get(1).size(); i++) {
			System.out.print(Integer.toHexString(cipher.get(1).get(i))+ " ");
		}
		System.out.println("\nInverse of KM: " + decrypted.get(0));
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<decrypted.get(1).size(); i++) {
			int x = decrypted.get(1).get(i);
			sb.append((char)x);
		}
		System.out.println("Decrypted Text: " + sb.toString());
	}

}
