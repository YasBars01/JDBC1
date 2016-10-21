package jdbcdemo;

import java.math.BigInteger;

public class test {

	public static void foo(int[] x, int a, int b, int i, int j){
		int k = j;
		int ct = 0;
		
		while (k > i-1 ){
			if (  x[k] <= b ) {
				if ( ! (x[k] <= a) ){
					ct = ct + 1;
				}
				
			}
			k = k - 1;
		}
		System.out.println(ct);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] x = {11, 10, 10, 5, 10,15, 20, 10, 7, 11};
		int [] y = null;
		BigInteger m = null;
		/*
		while ( m < 1000000000000000000 ){
			y[m] = m;
			m = m +1;
		}
		*/
		foo(x,8,18,3,6);
		foo(x,10,20,0,9);
		foo(x,8,18,6,3);
		foo(x,20,10,0,9);
		foo(x,6,7,8,8);
		System.out.println(1%2);

	}

}
