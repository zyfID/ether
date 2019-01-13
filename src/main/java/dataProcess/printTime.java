package dataProcess;

import java.util.Date;

public class printTime {
	public static void main(String[] args) {
		long a = System.currentTimeMillis();
		System.out.println(a);
		a =a +1000;
		long b;
		while(a>(b=System.currentTimeMillis())) { //604800000
			System.out.println(b);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
