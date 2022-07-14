package com.hung.Util;

import java.time.LocalTime;

public class DateTimeUtil {

	public DateTimeUtil() {
		// TODO Auto-generated constructor stub
	}

	public static LocalTime doubleToTime(double number) {
		String numstring = String.valueOf(number);
		String[] split = numstring.split("\\.");
		LocalTime time = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1])*6);
		return time;
	}
}
