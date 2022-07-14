package com.hung.CustomConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, LocalDate>{

	@Override
	public LocalDate convert(String source) {
		String sanitized = source.replaceAll("[\\D&&[^-]]", "");
		if(sanitized.isEmpty()) {sanitized = "1945-01-01";}
		return  LocalDate.parse(sanitized, DateTimeFormatter.ofPattern("yyyy-MM-dd"));	
	}

}
