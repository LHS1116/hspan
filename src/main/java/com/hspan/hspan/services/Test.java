package com.hspan.hspan.services;

import java.util.Scanner;

public class Test {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		String a;
		a = scanner.nextLine();
		while(!a.equals("QUIT"))  {
			a = scanner.nextLine();
		}
		System.out.println("----- Good Bye! -----");
		System.exit(0);
	}
	}
