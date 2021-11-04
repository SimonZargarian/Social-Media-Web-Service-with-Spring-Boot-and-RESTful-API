package com.kokabmedia.rest.ws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * This class is used to create a runtime exception 400 Not Found
 * in the retrieveUser() of the UserResource class.
 * 
 * @ResponseStatus annotation enables the display of exception error 
 * message 400 Not Found in the HTTP response body with the Spring Boot
 * framework and Spring MVC auto configuration exception handling.
 * */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message) {
		super(message);

	}
	
	

}
