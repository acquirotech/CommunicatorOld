package com.acq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {
	
	private static Pattern pattern;
	private static Matcher matcher;
	
	private static final String frequencyPattern="^[a-zA-Z]{1,5}$";

	public static boolean isFrequency(String frequency) {
		 if(frequency==""||frequency==null||frequency.equals("")){
		      return false;
		 }else{
			 if(frequency.equals("today")||frequency.equals("week")||frequency.equals("month"))
				 return true;
			 else
				 return false;
		 }
	}
	public static boolean dateValidation(String fromDate, String toDate)
	{ if (fromDate.matches("^((20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")&&toDate.matches("^((20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")) {
	     
		  return true;
		  }
		  else 
		  return false;
	}

	public static boolean isDateValidation(String fromDate, String toDate)
	{
		if (fromDate.compareTo(toDate) <= 0) {
			return true;
		}
		else
			return false;
	}
	
	
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
	public StringValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	public static boolean validate(final String hex) { 
		  if(hex==null||hex==""||hex.equals("")){
			  return false;
		  }
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
}
