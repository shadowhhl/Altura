package model;

import java.text.NumberFormat;
import java.util.Locale;

public class Formater {
	static public String toShortDouble(double d) {
		if (Double.isNaN(d)) {
			return "NaN";
		}
		else {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(1);
			return nf.format(d);
		}
	}
	
	static public String toShortDouble(double d, int franctionDigits) {
		if (Double.isNaN(d)) {
			return "NaN";
		}
		else {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(franctionDigits);
			nf.setMinimumFractionDigits(franctionDigits);
			return nf.format(d);
		}
	}
	
	static public String toCurrency(double d) {
		if (Double.isNaN(d)) {
			return "NaN";
		}
		else {
			NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
			return nf.format(d);
		}
		
	}
	
	static public String toCurrency(Double d) {
		if (d.isNaN()) {
			return "NaN";
		} else {
			return toCurrency(d.doubleValue());
		}
	}
	
	static public String currencyToString(String c) {
		String t = c;
		t = t.replace("$", "");
		t = t.replace(",", "");
		return t;
	}
	
	static public String toPercentage(double d) {
		if (Double.isNaN(d)) {
			return "NaN";
		}
		else {
			NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
			return nf.format(d);
		}
	}

	static public String toDateString(int year, int month, int day) {
		StringBuffer sb = new StringBuffer();
		return sb.append(month).append("-").append(day).append("-").append(year).toString();
	}
	
	static public String toDateString(int year, int month) {
		StringBuffer sb = new StringBuffer();
		return sb.append(month).append("-").append(year).toString();
	}

	static public Double annualToMonth(Double annualRate) {
		return 100.0*(Math.pow(1+annualRate/100.0, 1.0/12.0)-1.0);
	}
	
	static public double annualToMonth(double annualRate) {
		return 100.0*(Math.pow(1+annualRate/100.0, 1.0/12.0)-1.0);
	}
}
