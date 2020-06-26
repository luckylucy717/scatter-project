package com.lucy.scatter.service.impl;

import java.security.SecureRandom;
import java.util.Random;

public class Test {
	
	private static SecureRandom random = new SecureRandom();
	
	public static final int tokenLen = 3;
	public static final String randomStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static void main(String[] args){
		for(int i=0 ; i< 10 ; i++){
			System.out.println(generate());
		}
		
	}
	public static String generate() {
        StringBuilder sb = new StringBuilder(tokenLen);
        for (int i = 0; i < tokenLen; i++) {
            sb.append( randomStr.charAt(random.nextInt(randomStr.length())));
        }
        return sb.toString();
    }
}
