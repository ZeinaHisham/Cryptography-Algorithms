import java.util.HashMap;
import java.util.Map;

public class StringOperations {
	
	public static String hexaToText(String s) {
		StringBuilder sb = new StringBuilder();
		String []split = s.split("(?<=\\G.{" + 2 + "})");
		for(int i=0; i<split.length; i++) {
			int dec = Integer.parseInt(split[i],16);
			sb.append((char)dec);
		}
		
		return sb.toString();
	}
	
	public static boolean isHexa(String s) {
		boolean valid = true;

		char[] a = s.toCharArray();

		for (char c: a)
		{
		    valid = ((c >= 'A') && (c <= 'F')) || 
		    		((c >= 'a') && (c <= 'f')) ||
		            ((c >= '0') && (c <= '9'));

		    if (!valid)
		    {
		        break;
		    }
		}

		return valid;
	}
	
	public static String xorHex(String s1, String s2) throws Exception {
		StringBuilder res = new StringBuilder();

		if(s1.length()==s2.length()) {
			for(int i=0; i<s1.length(); i++) {
				String x = String.valueOf(s1.charAt(i));
				String y = String.valueOf(s2.charAt(i));
				
				int xInt = Integer.parseInt(x, 16);
				int yInt = Integer.parseInt(y, 16);
				
				int xor = xInt ^ yInt;
				String h = Integer.toHexString(xor);
				res.append(h);
			}
		}
		else {
			throw new Exception("XOR lengths not equal");
		}
		
		return res.toString();
	}
}
