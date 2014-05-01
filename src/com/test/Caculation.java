/**
 * 
 */
package com.test;

/**
 * @author xiao for test? ?.....
 *
 */
public class Caculation {
	public static void calculate() {
		calculate(3000);
	}

	public static void calculate(int parseInt) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(parseInt);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
