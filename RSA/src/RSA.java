import java.util.ArrayList;

public class RSA {
	public static long squareAndMultiply(long exp, long x, long n) {
		String binary = Long.toBinaryString(exp);
		Long y = x;
		
		for(int i=1; i<binary.length(); i++) {
			y = y * y;
			y = Math.floorMod(y, n);
			
			char t = binary.charAt(i);
			if(t=='1') {
				y = y * x;
				y = Math.floorMod(y, n);
			}	
		}
		return y;
	}
	
	public static ArrayList<Long> encrypt(int[] text, int e, int n) {
		ArrayList<Long> encrypted = new ArrayList<Long>();
		for(int i=0; i<text.length; i++) {
			Long cipherDec = squareAndMultiply(e, text[i], n);
			encrypted.add(cipherDec);
		}
		return encrypted;
	}
	
	public static String decrypt(ArrayList<Long> cipher, int p, int q, int d, int n) {
		//Step 1 - CRT Transformation
		long[] xps = new long[cipher.size()];
		long[] xqs = new long[cipher.size()];
		
		for(int i=0; i<cipher.size(); i++) {
			long xp = squareAndMultiply(1, cipher.get(i), p);
			long xq = squareAndMultiply(1, cipher.get(i), q);
			xps[i] = xp; xqs[i] = xq;
		}
		
		//Step 2 - Exponentiation in CRT Domain
		long dp = squareAndMultiply(1, d, p-1);
		long dq = squareAndMultiply(1, d, q-1);
		long[] yps = new long[cipher.size()];
		long[] yqs = new long[cipher.size()];
		
		for(int i=0; i<cipher.size(); i++) {
			long yp = squareAndMultiply(dp, xps[i], p);
			long yq = squareAndMultiply(dq, xqs[i], q);
			yps[i] = yp; yqs[i] = yq;
		}
		
		//Step 3 - Inverse CRT Transformation
		int[] res = KeyGen.gcd(p, q);
		long cp = (q < p) ? res[1] : res[0];
		long cq = (q < p) ? res[0] : res[1];
		
		long[] y = new long[cipher.size()];
		StringBuilder decrypted = new StringBuilder();
		
		for(int i=0; i<cipher.size(); i++) {
			long dec = (q * cp) * yps[i] + (p * cq) * yqs[i];
			y[i] = squareAndMultiply(1, dec, n);
			decrypted.append(Character.toString((char) y[i]));
		}
		return decrypted.toString();
	}
	
}
