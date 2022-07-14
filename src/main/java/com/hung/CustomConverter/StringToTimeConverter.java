package com.hung.CustomConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class StringToTimeConverter implements Converter<String, LocalTime>{

	@Override
	public LocalTime convert(String source) {
		String sanitized = source.replaceAll("[\\D&&[^:]]", "");
		if(sanitized.isEmpty()) {sanitized = "00:00";}
		
		return LocalTime.parse(sanitized, DateTimeFormatter.ofPattern("HH:mm"));
	}

}
