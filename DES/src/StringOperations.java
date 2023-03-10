
public class StringOperations {
	public static String toBinary(String key) {
		byte[] bytes = key.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes)
		{
		   int val = b;
		   for (int i = 0; i < 8; i++)
		   {
		      binary.append((val & 128) == 0 ? 0 : 1);
		      val <<= 1;
		   }
		}
		
		return binary.toString();
	}
	
	public static String binaryToString(String key) {
		String[] bytesStr = key.split("(?<=\\G.{" + 8 + "})");
		
		StringBuilder finalResult = new StringBuilder();
		for (String s : bytesStr) {
			finalResult.append((char) Integer.parseInt(s, 2));
		}
		
		return finalResult.toString();
	}
	
	public static String stringShiftLeft(String s, int n) {
		StringBuilder res = new StringBuilder(s);
		
		if(n==1) {
			char first = res.charAt(0);
			res.deleteCharAt(0);
			res.append(first);
		}
		else if(n==2) {
			char first = s.charAt(0);
			char second = s.charAt(1);
			res.deleteCharAt(0);
			res.deleteCharAt(0);
			res.append(first);
			res.append(second);
		}
		
		return res.toString();
	}
	
	public static String stringShiftRight(String s, int n) {
		StringBuilder res = new StringBuilder(s);
		
		if(n==1) {
			char last = res.charAt(s.length()-1);
			res.deleteCharAt(s.length()-1);
			res.insert(0, last);
		}
		else if(n==2) {
			char last = s.charAt(s.length()-1);
			char secondlast = s.charAt(s.length()-2);
			res.deleteCharAt(s.length()-1);
			res.deleteCharAt(s.length()-2);
			res.insert(0, last);
			res.insert(0, secondlast);
		}
		
		return res.toString();
	}
	
	public static String XOR(String s, String t) {
		StringBuilder res = new StringBuilder();
		for(int i=0; i<s.length(); i++) {
			if(s.charAt(i)==t.charAt(i)) {
				res.append("0");
			}
			else {
				res.append("1");
			}
		}
		
		return res.toString();
	}
	
	public static String fourBits(String s) {
		return ("0000" + s).substring(s.length());
	}
}
