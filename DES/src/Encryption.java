import java.util.ArrayList;

public class Encryption {
	public static String initialPermutation(String key) {
		StringBuilder ip = new StringBuilder();
		for(int i=0; i<64; i++) {
			int pos = Tables.IP[i];
			ip.append(key.charAt(pos-1));
		}
		
		return ip.toString();
	}
	
	public static String finalPermutation(String key) {
		StringBuilder fp = new StringBuilder();
		for(int i=0; i<64; i++) {
			int pos = Tables.IP1[i];
			fp.append(key.charAt(pos-1));
		}
		
		return fp.toString();
	}
	
	public static String expansion(String key) {
		StringBuilder exp = new StringBuilder();
		for(int i=0; i<48; i++) {
			int pos = Tables.E[i];
			exp.append(key.charAt(pos-1));
		}
		
		return exp.toString();
	}
	
	public static String permutation(String key) {
		StringBuilder p = new StringBuilder();
		for(int i=0; i<32; i++) {
			int pos = Tables.P[i];
			p.append(key.charAt(pos-1));
		}
		
		return p.toString();
	}
	
	
	public static String fFunction(String left, String right, String key) {
		//1-Expand
		right = expansion(right);
		
		//2- XOR
		String xor = StringOperations.XOR(right, key);
		
		//3- S-Boxes - split into 6 * 8
		String []split = xor.split("(?<=\\G.{" + 6 + "})");
		
		StringBuilder sBoxResult = new StringBuilder();
		
		for(int i=0; i<8; i++) {	
			StringBuilder outer = new StringBuilder();
			outer.append(split[i].charAt(0));
			outer.append(split[i].charAt(5));
			String inner = split[i].substring(1, 5);
			
			int row = Integer.parseInt(outer.toString(), 2);
			int column = Integer.parseInt(inner, 2);
			int pos = (16*row) + column;
			
			String sBox = StringOperations.fourBits(Integer.toBinaryString(Tables.returnSBox(i+1, pos)));
			sBoxResult.append(sBox);
		}

		String p = permutation(sBoxResult.toString());
		//End of f-Function
		
		//4- XOR 
		String xor2 = StringOperations.XOR(left, p);
		
		return xor2;
	}
	
	public static ArrayList<String> encryptText(ArrayList<String> textArray, ArrayList<String> roundKeys) {
		ArrayList<String> plaintext = new ArrayList<String>();
		
		//Change to Binary
		for(int i=0; i<textArray.size(); i++) {
			String binary = StringOperations.toBinary(textArray.get(i));
			plaintext.add(binary);
		}
		
		//Initial Permutation
		ArrayList<String> ip1 = new ArrayList<String>();
		for(int i=0; i<plaintext.size(); i++) {
			String ip = initialPermutation(plaintext.get(i));
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
				String newRight = fFunction(left.get(j), right.get(j), roundKeys.get(j));
				String oldRight = right.get(j);
				
				right.add(newRight);
				left.add(oldRight);
			}
			
			//System.out.println(right);
			//System.out.println(left);
			
			//Final Permutation
			StringBuilder rightLeft = new StringBuilder(right.get(16));
			rightLeft.append(left.get(16));
			fp.add(finalPermutation(rightLeft.toString()));
		}
		
		return fp;
	}

}
