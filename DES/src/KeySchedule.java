import java.util.ArrayList;

public class KeySchedule {

	public static String pc1(String key) {
		StringBuilder permutated1 = new StringBuilder();
		for(int i=0; i<56; i++) {
			int pos = Tables.PC1[i];
			permutated1.append(key.charAt(pos-1));
		}
		
		return permutated1.toString();
	}
	
	public static String pc2(String key) {
		StringBuilder permutated2 = new StringBuilder();
		for(int i=0; i<48; i++) {
			int pos = Tables.PC2[i];
			permutated2.append(key.charAt(pos-1));
		}
		
		return permutated2.toString();
	}
	
	public static ArrayList<String> leftShift(ArrayList<String> arr, String c, String d)
	{	
		int i=1;
		while(i<=16) {
			if(i==1 || i==2 || i==9 || i==16) {
				c = StringOperations.stringShiftLeft(c, 1);
				d = StringOperations.stringShiftLeft(d, 1);
			}
			else {
				c = StringOperations.stringShiftLeft(c, 2);
				d = StringOperations.stringShiftLeft(d, 2);
			}
			String cd = c.concat(d);
			arr.add(pc2(cd));
			i++;
		}
		
		return arr;
	}
	
	public static ArrayList<String> generateEncryptionKeys(String key) {
		ArrayList<String> roundKeys = new ArrayList<String>();
		
		//Change to Binary
		key = StringOperations.toBinary(key);
		
		//Permutated Choice 1
		String pc1 = pc1(key);
		
		//Left Shift and Permutated Choice 2
		String C = pc1.substring(0, 28);
		String D = pc1.substring(28);
		roundKeys = leftShift(roundKeys, C, D);
		
		return roundKeys;
	}
	
	public static ArrayList<String> rightShift(ArrayList<String> arr, String c, String d)
	{	
		int i=1;
		while(i<=16) {
			if(i==2 || i==9 || i==16) {
				c = StringOperations.stringShiftRight(c, 1);
				d = StringOperations.stringShiftRight(d, 1);
			}
			else if(i!=1) {
				c = StringOperations.stringShiftRight(c, 2);
				d = StringOperations.stringShiftRight(d, 2);
			}
			String cd = c.concat(d);
			arr.add(pc2(cd));
			i++;
		}
		
		return arr;
	}
	
	public static ArrayList<String> generateDecryptionKeys(String key) {
		ArrayList<String> roundKeys = new ArrayList<String>();
		
		//Change to Binary
		key = StringOperations.toBinary(key);
		
		//Permutated Choice 1
		String pc1 = pc1(key);
		
		
		//Right Shift and Permutated Choice 2
		String C = pc1.substring(0, 28);
		String D = pc1.substring(28);
		roundKeys = rightShift(roundKeys, C, D);
		
		return roundKeys;
	}

}
