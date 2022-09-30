package com.capgemini.UniversityCourseSelection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice 
public class MyExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionDescription> handleNotFoundException(NotFoundException myException, WebRequest request){
		ExceptionDescription descript = new ExceptionDescription(myException.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ExceptionDescription>(descript, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NotLoggedInException.class)
	public ResponseEntity<ExceptionDescription> handleNotLoggedInException(NotLoggedInException myException, WebRequest request){
		ExceptionDescription descript = new ExceptionDescription(myException.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ExceptionDescription>(descript, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDescription> handleAnyException(Exception exception, WebRequest request){
		ExceptionDescription descript = new ExceptionDescription(exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<ExceptionDescription>(descript, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
