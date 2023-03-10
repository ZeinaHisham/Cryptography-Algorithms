import java.util.ArrayList;

public class Decryption {

	public static int Multiplication(String s, int n) {
		if(n==1) { //0B
			int x = Integer.parseInt(s,16);
			int y = x << 1;
			int z = x << 3;
			int p = z ^ y ^ x;
			while(true) {
				if(p>0x3FF)
					p = p ^ 0x46C;
				else if(p>0x2FF && p<=0x3FF)
					p = p ^ 0x236;
				else if(p>0xFF && p<=0x2FF)
					p = p ^ 0x11B;
				else 
					break;
			}
			return p;
		}
		else if(n==2) { //0E
			int x = Integer.parseInt(s,16);
			int y = x << 1;
			int m = x << 2;
			int z = x << 3;
			int p = z ^ m ^ y;
			while(true) {
				if(p>0x3FF)
					p = p ^ 0x46C;
				else if(p>0x2FF && p<=0x3FF)
					p = p ^ 0x236;
				else if(p>0xFF && p<=0x2FF)
					p = p ^ 0x11B;
				else 
					break;
			}
			return p;
		}
		else if(n==3) { //09
			int x = Integer.parseInt(s,16);
			int z = x << 3;
			int p = z ^ x;
			while(true) {
				if(p>0x3FF)
					p = p ^ 0x46C;
				else if(p>0x2FF && p<=0x3FF)
					p = p ^ 0x236;
				else if(p>0xFF && p<=0x2FF)
					p = p ^ 0x11B;
				else 
					break;
			}
			return p;
		}
		else if(n==4) { //0D
			int x = Integer.parseInt(s,16);
			int m = x << 2;
			int z = x << 3;
			int p = z ^ m ^ x;
			while(true) {
				if(p>0x3FF)
					p = p ^ 0x46C;
				else if(p>0x2FF && p<=0x3FF)
					p = p ^ 0x236;
				else if(p>0xFF && p<=0x2FF)
					p = p ^ 0x11B;
				else 
					break;
			}
			return p;
		}
		return -1;
	}
	
	public static String MixColumns(String s1, String s2, String s3, String s4, int n) {
		if(n==1) {
			int C0 = Multiplication(s1,2);	
			int C1 = Multiplication(s2,1);
			int C2 = Multiplication(s3,4);
			int C3 = Multiplication(s4,3);
			int B0 = C0 ^ C1 ^ C2 ^ C3;
			return String.format("%02X", B0);
		}
		else if(n==2) {
			int C0 = Multiplication(s1,3);	
			int C1 = Multiplication(s2,2);
			int C2 = Multiplication(s3,1);
			int C3 = Multiplication(s4,4);
			int B1 = C0 ^ C1 ^ C2 ^ C3;
			return String.format("%02X", B1);
		}
		else if(n==3) {
			int C0 = Multiplication(s1,4);	
			int C1 = Multiplication(s2,3);
			int C2 = Multiplication(s3,2);
			int C3 = Multiplication(s4,1);
			int B2 = C0 ^ C1 ^ C2 ^ C3;
			return String.format("%02X", B2);
		}
		else if(n==4) {
			int C0 = Multiplication(s1,1);	
			int C1 = Multiplication(s2,4);
			int C2 = Multiplication(s3,3);
			int C3 = Multiplication(s4,2);
			int B3 = C0 ^ C1 ^ C2 ^ C3;
			return String.format("%02X", B3);
		}
		
		return null;
	}
	
	public static String AESLayers(String key,String []split, int round) throws Exception {
		//Mix Columns
		StringBuilder mixCol = new StringBuilder();
		if(round!=1) {
			for(int i=0; i<split.length; i+=4)
			{
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 1));
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 2));
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 3));
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 4));
			}
		}
		else {
			mixCol.append(String.join("", split));
		}
		
		
		split = mixCol.toString().split("(?<=\\G.{" + 2 + "})");
		
		//Shift Rows
		StringBuilder shiftRows = new StringBuilder();
		shiftRows.append(split[0] + split [13] + split [10] + split[7]);
		shiftRows.append(split[4] + split [1] + split [14] + split[11]);
		shiftRows.append(split[8] + split [5] + split [2] + split[15]);
		shiftRows.append(split[12] + split [9] + split [6] + split[3]);
		
		split = shiftRows.toString().split("(?<=\\G.{" + 2 + "})");
		
		//Byte Substitution
		StringBuilder sBox = new StringBuilder();
		for(int i=0; i<split.length; i++) {
			String x = String.valueOf(split[i].charAt(0));
			String y = String.valueOf(split[i].charAt(1));
			
			
			int row = Integer.parseInt(x, 16);
			int column = Integer.parseInt(y, 16);
			int pos = (16*row) + column;
			
			int sub = Tables.invsBox[pos];
			String h = String.format("%02X", sub);
			sBox.append(h);
		}
		String decryptedText = StringOperations.xorHex(key, sBox.toString());
		
		return decryptedText;
	}
	
	public static String decryptText(ArrayList<String> encryptedText, ArrayList<String> roundKeys) throws Exception{
		ArrayList<String> decrypted = new ArrayList<String>();
		
		//Initial Key Addition
		ArrayList<String> InitialAdd = new ArrayList<String>();
		for(int i=0; i<encryptedText.size(); i++) {
			InitialAdd.add(StringOperations.xorHex(roundKeys.get(10), encryptedText.get(i)));
		}
		
		
		for(int i=0; i<InitialAdd.size(); i++) {
			//Split into 16-bytes
			String []split = InitialAdd.get(i).split("(?<=\\G.{" + 2 + "})");
			
			for(int j=1; j<=10; j++) {
				String round = AESLayers(roundKeys.get(10-j), split, j);
				split = round.split("(?<=\\G.{" + 2 + "})");
				if(j==10) decrypted.add(round);
			}

		}
		
		System.out.println("Decrypted Text in Hexa: " + decrypted);
		
		System.out.print("Decrypted Text in Characters: ");
		for(int i=0; i<decrypted.size(); i++) {
			System.out.print(StringOperations.hexaToText(decrypted.get(i)) + " ");
		}
		
		return null;
	}
}
