package com.hospital.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class GeneralUtil {
	
	public String getRandomNumber6DigitString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    return String.format("%06d", number);
	}
	

}
