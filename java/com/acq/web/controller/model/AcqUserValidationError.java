package com.acq.web.controller.model;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * 
 * @author Aranoah Tech
 * Class to convert validation error returned form hibernate validator to simple error message
 *
 */
public class AcqUserValidationError {

	String field;
	String message;	
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @param hValidatorErrors - Set which was returned by validation using hibernate validator
	 * @return List   -- List of UserValidation Error containing message and description for each error field
	 */

	public static ArrayList<AcqUserValidationError> createUserValidationErrorSet(Set hValidatorErrors){
		
	   ArrayList<AcqUserValidationError> returnSet= new ArrayList<AcqUserValidationError>();
	   for(Object errorGenericObject: hValidatorErrors){
		   ConstraintViolation error = (ConstraintViolation) errorGenericObject;
		   AcqUserValidationError errorObject= new AcqUserValidationError();
		   errorObject.setField(error.getPropertyPath().toString());
		   errorObject.setMessage(error.getMessage());
		   returnSet.add(errorObject);
	   }
	   
	   return returnSet;
	}
	
}
