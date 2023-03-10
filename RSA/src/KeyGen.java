import java.util.Random;

public class KeyGen {
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
	
	public static boolean isPrime(int p, int s) {
		Random rand = new Random();
		
		for(int i=0; i<s; i++) {
			int a = rand.nextInt((p-2)-2) + 2;
			
			int power = (int) RSA.squareAndMultiply(p-1, a, p);
			if(power != 1)
				return false;
		}
		return true;
	}
	
	public static int[] generateKeys() {
		Random rand = new Random();
		
		int p;
		while(true) {
			p = rand.nextInt(32767-128) + 128;
			if(isPrime(p, 100))
				break;
		}
		
		int q;
		while(true) {
			q = rand.nextInt(32767-128) + 128;
			if(isPrime(q, 100))
				break;
		}
		
		int n = p * q;
		int phi = (p-1) * (q-1);
		
		int e,d;
		while(true) {
			e = rand.nextInt((phi-1)-2) - 2;
			int[] res = gcd(phi, e);
			if(res[2]==1) {
				d = Math.floorMod(res[1], phi);
				break;
			}
		}
		
		
		int[] res = { p, q, n, e , d };
		return res;
	}
}
