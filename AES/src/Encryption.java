import java.util.ArrayList;

public class Encryption {

	public static int Multiplication(String s, int n) {
		if(n==2) {
			int x = Integer.parseInt(s, 16);
			x = x << 1;
			if(x>255) 
				x = x ^ 0x11B;
			return x;
		}
		else if(n==3) {
			int x = Integer.parseInt(s,16);
			int y = x << 1;
			y = y ^ x;
			if(y>255)
				y = y ^ 0x11B;
			return y;
		}

		return -1;
	}
	
	public static String MixColumns(String s1, String s2, String s3, String s4, int n) {
		if(n==1) {
			int B0 = Multiplication(s1, 2);
			int B5 = Multiplication(s2, 3);
			int B10 = Integer.parseInt(s3, 16);
			int B15 = Integer.parseInt(s4, 16);
			int C0 = B0 ^ B5 ^ B10 ^ B15;		
			return String.format("%02X", C0);
		}
		else if(n==2) {
			int B0 = Integer.parseInt(s1, 16);
			int B5 = Multiplication(s2, 2);
			int B10 = Multiplication(s3, 3);
			int B15 = Integer.parseInt(s4, 16);
			int C1 = B0 ^ B5 ^ B10 ^ B15;
			return String.format("%02X", C1);
		}
		else if(n==3) {
			int B0 = Integer.parseInt(s1, 16);
			int B5 = Integer.parseInt(s2, 16);
			int B10 = Multiplication(s3, 2);
			int B15 = Multiplication(s4, 3);
			int C2 = B0 ^ B5 ^ B10 ^ B15;
			return String.format("%02X", C2);
		}
		else if(n==4) {
			int B0 = Multiplication(s1, 3);
			int B5 = Integer.parseInt(s2, 16);
			int B10 = Integer.parseInt(s3, 16);
			int B15 = Multiplication(s4, 2);
			int C3 = B0 ^ B5 ^ B10 ^ B15;
			return String.format("%02X", C3);
		}
		
		return null;
	}
	
	public static String AESLayers(String key,String []split, int round) throws Exception {
		//Byte Substitution
		StringBuilder sBox = new StringBuilder();
		for(int i=0; i<split.length; i++) {
			String x = String.valueOf(split[i].charAt(0));
			String y = String.valueOf(split[i].charAt(1));
			
			
			int row = Integer.parseInt(x, 16);
			int column = Integer.parseInt(y, 16);
			int pos = (16*row) + column;
			
			int sub = Tables.sBox[pos];
			String h = String.format("%02X", sub);
			sBox.append(h);
		}
		
		split = sBox.toString().split("(?<=\\G.{" + 2 + "})");
		
		//Shift Rows
		StringBuilder shiftRows = new StringBuilder();
		shiftRows.append(split[0] + split [5] + split [10] + split[15]);
		shiftRows.append(split[4] + split [9] + split [14] + split[3]);
		shiftRows.append(split[8] + split [13] + split [2] + split[7]);
		shiftRows.append(split[12] + split [1] + split [6] + split[11]);
		
		split = shiftRows.toString().split("(?<=\\G.{" + 2 + "})");
		
		//Mix Columns
		StringBuilder mixCol = new StringBuilder();
		if(round!=10) {
			for(int i=0; i<split.length; i+=4)
			{
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 1));
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 2));
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 3));
				mixCol.append(MixColumns(split[i],split[i+1],split[i+2],split[i+3], 4));
			}
		}
		else {
			mixCol = shiftRows;
		}
		
		//Key Addition
		String encryptedText = StringOperations.xorHex(key, mixCol.toString());
		return encryptedText;
	}
	
	public static ArrayList<String> encryptText(ArrayList<String> textArray, ArrayList<String> roundKeys) throws Exception {
		ArrayList<String> encrypted = new ArrayList<String>();

		//Convert to Hexadecimal
		ArrayList<String> hexaText = new ArrayList<String>();
		for(int i=0; i<textArray.size(); i++) {
			  StringBuilder s = new StringBuilder();
		      char ch[] = textArray.get(i).toCharArray();
		      for(int j= 0; j < ch.length; j++) {
		         String hexString = Integer.toHexString(ch[j]);
		         s.append(hexString);
		      }
		      
		      hexaText.add(s.toString());
		}
		
		//System.out.println(StringOperations.hexaToText(hexaText.get(0)));
		
		ArrayList<String> InitialAdd = new ArrayList<String>();
		//Initial Key Addition
		for(int i=0; i<hexaText.size(); i++) {
			InitialAdd.add(StringOperations.xorHex(roundKeys.get(0), hexaText.get(i)));
		}
		
		//10 Rounds
		String finalEnc = "";
		for(int i=0; i<InitialAdd.size(); i++) {
			//Split into 16-bytes
			String []split = InitialAdd.get(i).split("(?<=\\G.{" + 2 + "})");
			
			for(int j=1; j<=10; j++) {
				String round = AESLayers(roundKeys.get(j), split,j);
				split = round.split("(?<=\\G.{" + 2 + "})");
				if(j==10) encrypted.add(round);
			}
		}
		
		//Display Characters in Hexadecimal
		System.out.println("Encrypted Text in Hexa: " + encrypted);
		
		//Display Characters in ASCII
		System.out.print("Encrypted Text in Characters: ");
		for(int i=0; i<encrypted.size(); i++) {
			System.out.print(StringOperations.hexaToText(encrypted.get(i)) + " ");
		}
		System.out.println();
		
		return encrypted;
	}
}
