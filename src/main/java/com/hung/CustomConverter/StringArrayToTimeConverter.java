package com.hung.CustomConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class StringArrayToTimeConverter implements Converter<String[], LocalTime>{

	@Override
	public LocalTime convert(String[] source) {
		for(int i = 0; i < source.length; i++) {
			source[i] = source[i].replaceAll("[\\D&&[^:]]", "");
			if(source[i].isEmpty()) {source[i] = "00";}
		}
		
		String input = source[0] + ":" + source[1];
		
		return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
	}
	
}
