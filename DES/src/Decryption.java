import java.util.ArrayList;

public class Decryption {
	public static String decryptText(ArrayList<String> encryptedText, ArrayList<String> roundKeys) {
		
		//Initial Permutation
		ArrayList<String> ip1 = new ArrayList<String>();
		for(int i=0; i<encryptedText.size(); i++) {
			String ip = Encryption.initialPermutation(encryptedText.get(i));
			ip1.add(ip);
		}
		
		//f-Function
		ArrayList<String> fp = new ArrayList<String>();
		
		for(int i=0; i<ip1.size(); i++) {
			ArrayList<String> right = new ArrayList<String>();
			ArrayList<String> left = new ArrayList<String>();
			
			String l = ip1.get(i).substring(0, 32);
			String r = ip1.get(i).substring(32);
			right.add(r);
			left.add(l);
			
			for(int j=0; j<16; j++) {
				String newRight = Encryption.fFunction(left.get(j), right.get(j), roundKeys.get(j));
				String oldRight = right.get(j);
				right.add(newRight);
				left.add(oldRight);
			}
			
			//Final Permutation
			StringBuilder leftright = new StringBuilder(right.get(16));
			leftright.append(left.get(16));
			fp.add(Encryption.finalPermutation(leftright.toString()));
		}
		
		StringBuilder decryptedText = new StringBuilder();
		for(int i=0; i<fp.size(); i++) {
			decryptedText.append(StringOperations.binaryToString(fp.get(i)));
		}
		
		return decryptedText.toString();
	}
}
