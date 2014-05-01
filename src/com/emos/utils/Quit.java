package com.emos.utils;

public class Quit {
	public static final void quit() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
