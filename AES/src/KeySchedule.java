import java.util.ArrayList;

public class KeySchedule {
	
	static String wordRotation(String key, int round) {
		String []split = key.split("(?<=\\G.{" + 8 + "})");
		/*for(int i=0; i<split.length;i++)
			System.out.println(split[i]);
		
		System.out.println();*/
		
		//Rotation
		String last = split[3];
		StringBuilder rotate = new StringBuilder();
		rotate.append(last.substring(2));
		rotate.append(last.substring(0, 2));
		last = rotate.toString();;
		
		//Substitution
		StringBuilder gFunc = new StringBuilder();

		for(int i=0; i<8; i+=2) {
			String x = String.valueOf(last.charAt(i));
			String y = String.valueOf(last.charAt(i+1));
			
			int row = Integer.parseInt(x, 16);
			int column = Integer.parseInt(y, 16);
			int pos = (16*row) + column;
			int sub = Tables.sBox[pos];
			
			//XOR with Round Constant
			if(i==0) {
				sub = sub ^ Tables.rC[round];
			}
			
			String h = String.format("%02X", sub);
			gFunc.append(h);
		}
		//XOR (4 Word Sub-Key)
		StringBuilder finalKey = new StringBuilder();
		
		String W3new = gFunc.toString();
		String W3 = split[3];
		String W2 = split[2];
		String W1 = split[1];
		String W0 = split[0];
		
		try {
			W0 = StringOperations.xorHex(W3new, W0);
			W1 = StringOperations.xorHex(W1, W0);
			W2 = StringOperations.xorHex(W2, W1);
			W3 = StringOperations.xorHex(W3, W2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finalKey.append(W0);
		finalKey.append(W1);
		finalKey.append(W2);
		finalKey.append(W3);
		
		return finalKey.toString();
	}
	
	public static ArrayList<String> generateEncryptionKeys(String key) {
		ArrayList<String> roundKeys = new ArrayList<String>();
		roundKeys.add(key); //Key 0
		
		for(int i=0; i<10; i++) {
			key = wordRotation(key, i);
			roundKeys.add(key);
		}
		
		System.out.println("Round Keys: " + roundKeys);
		return roundKeys;
	}
}
