import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class ElGamal {
	public static int[] gcd(int a, int b) {
		if(a < b) { int temp = b; b = a; a = temp; }
		
		int[] s = { 1, 0 };
		int[] t = { 0, 1 };
		int[] r = { a, b };
		
		int qi, si = 0, ti = 0;
		while(true) {
			int ri = Math.floorMod(r[0], r[1]);
			if(ri == 0)
				break;
			
			qi = (r[0] - ri) / r[1];
			si = s[0] - ( qi * s[1] );
			ti = t[0] - ( qi * t[1] );
			

			r[0] = r[1]; r[1] = ri;
			s[0] = s[1]; s[1] = si;
			t[0] = t[1]; t[1] = ti;
		}
		
		int gcd = si * a + ti * b;
		int[] res = {si, ti, gcd};
		
		return res;
	}
	
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
	
	public static boolean isPrime(int p, int s) {
		Random rand = new Random();
		
		for(int i=0; i<s; i++) {
			int a = rand.nextInt((p-2)-2) + 2;
			
			int power = (int) squareAndMultiply(p-1, a, p);
			if(power != 1)
				return false;
		}
		return true;
	}
	
	public static int primitiveRoots(int p) {		
		Random rand = new Random();
		
		ArrayList<Integer> factors = new ArrayList<Integer>();
		for(int i=1; i<=p; i++) {
			if((p-1) % i == 0 && i>1) factors.add(i);
		}
		//System.out.println(factors);
		
		ArrayList<Integer> primitive = new ArrayList<Integer>();
		for(int i=2; i<p; i++) {
			for(int j=0; j<factors.size(); j++) {
				BigInteger e = BigInteger.valueOf(i);
				BigInteger k = e.pow(factors.get(j));
				
				k = k.mod(BigInteger.valueOf(p));
				//System.out.println("i: " + i + " factor: " + factors.get(j) + " k: " + k);
				if(k.equals(BigInteger.valueOf(1))) {
					if(factors.get(j)==p-1) 
						primitive.add(i);
						//return i;
					break;
				}
				
			}
		}
		
		int randomNum = rand.nextInt(primitive.size());
		
		return primitive.get(randomNum);
	}
	
	public static int[] GenerateKeys() {
		Random rand = new Random();
		
		int p;
		while(true) {
			p = rand.nextInt(32767-1) + 1;
			if(isPrime(p, 100))
				break;
		}
		
		int alpha = primitiveRoots(p);
		
		int d = rand.nextInt((p-2) - 2) + 2 ;
		
		BigInteger beta = BigInteger.valueOf(alpha).pow(d);
		beta = beta.mod(BigInteger.valueOf(p));
		
		int[] res = {p, alpha, beta.intValue(), d};
		
		return res;
	}
	
	public static ArrayList<ArrayList<Integer>> Encrypt(String plaintext, int[] param) {
		Random rand = new Random();
		int p = param[0], alpha = param[1], beta = param[2];
		
		ArrayList<Integer> kprA = new ArrayList<Integer>();
		for(int i=0; i<plaintext.length(); i++) {
			int j = rand.nextInt((p-2) - 2) + 2;
			kprA.add(j);
		}
		
		ArrayList<Integer> KE = new ArrayList<Integer>();
		for(int i=0; i<kprA.size(); i++) {
			BigInteger k = BigInteger.valueOf(alpha).pow(kprA.get(i));
			k = k.mod(BigInteger.valueOf(p));
			KE.add(k.intValue());
		}
		
		ArrayList<Integer> KM = new ArrayList<Integer>();
		for(int i=0; i<kprA.size(); i++) {
			BigInteger k = BigInteger.valueOf(beta).pow(kprA.get(i));
			k = k.mod(BigInteger.valueOf(p));
			KM.add(k.intValue());
		}
		
		ArrayList<Integer> cipher = new ArrayList<Integer>();
		for(int i=0; i<plaintext.length(); i++) {
			int c = (int) plaintext.charAt(i);
			long y  = c * KM.get(i);
			y = Math.floorMod(y, p);
			cipher.add((int)y);
		}
		
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		res.add(KE); res.add(cipher);
		
		return res;
	}
	
	public static ArrayList<ArrayList<Integer>> Decrypt(ArrayList<Integer> KE, ArrayList<Integer> cipher, int d, int p) {
		
		ArrayList<Integer> KM = new ArrayList<Integer>();
		for(int i=0; i<KE.size(); i++) {
			BigInteger k = BigInteger.valueOf(KE.get(i)).pow(d);
			k = k.mod(BigInteger.valueOf(p));
			KM.add(k.intValue());
		}
		
		ArrayList<Integer> KMinv = new ArrayList<Integer>();
		for(int i=0; i<KM.size(); i++) {
			int[] res = gcd(p, KM.get(i));
			int inv = Math.floorMod(res[1], p);
			KMinv.add(inv);
		}
		
		ArrayList<Integer> decrypted = new ArrayList<Integer>();
		for(int i=0; i<KMinv.size(); i++) {
			long x = cipher.get(i) * KMinv.get(i);
			x = Math.floorMod(x, p);
			decrypted.add((int)x);
		}
		
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		res.add(KMinv); res.add(decrypted);
		return res;
	}
}
