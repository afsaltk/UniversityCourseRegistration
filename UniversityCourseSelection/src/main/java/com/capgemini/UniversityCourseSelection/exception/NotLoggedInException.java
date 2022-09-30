package com.capgemini.UniversityCourseSelection.exception;

public class NotLoggedInException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public NotLoggedInException(String message) {
		super(message);
	}
	public NotLoggedInException() {
		
	}
}
