package com.hung.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import javax.el.Expression;

import org.hibernate.query.criteria.internal.CriteriaUpdateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static java.lang.System.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import com.hung.Models.Player;
import com.hung.Models.ReservationStatus;
import com.hung.Models.SportType;
import com.hung.Service.SportTypeService;
import com.hung.Service.SportTypeServiceImp;
import com.hung.Util.DateTimeUtil;
import com.oracle.wls.shaded.org.apache.xpath.axes.ReverseAxesWalker;

public class Test {

	
	public static void main(String[] args) {
		
//		String regex = "^(?!.*bar).*$";
//		Scanner read = new Scanner(in);
//		String input ;
//		do {
//			input = read.nextLine();
//			out.println(input.matches(regex));
//		}while(!read.equals("n"));
		
//		org.springframework.expression.ExpressionParser parser = new SpelExpressionParser();
//		org.springframework.expression.Expression ex = parser.parseExpression(" '32' matches '\\d+' ");
		
		int[] a = {4, 5, 7};
		
		out.println(Arrays.binarySearch(a, 7));
	}
	
	public static void change(testing test) {
		test.setA(0);
	}
}

class testing{
	private int a;
	private int b;
	private int c = updateC();

	
	public testing(int a, int b, int c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public int getA() {
		return a;
	}
	
	public void setA(int a) {
		this.a = a;
		updateC();
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
		updateC();
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	@Override
	public String toString() {
		return "testing [a=" + a + ", b=" + b + ", c=" + c + "]";
	}
	
	public int updateC() {
		return this.c = this.a*this.b;
	}
}