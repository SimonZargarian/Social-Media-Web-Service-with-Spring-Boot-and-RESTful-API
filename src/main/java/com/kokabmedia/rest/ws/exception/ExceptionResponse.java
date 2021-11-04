package com.kokabmedia.rest.ws.exception;

import java.util.Date;

/*
 * This class defines a standard structure for exception handling and 
 * error responses for all resources in the application.
 * 
 * We define the structure how error message are shown in the HTTP error
 * message response body. We will be able to send a response back in a 
 * specific format.
 */
public class ExceptionResponse {

	private Date timeStamp;
	private String message;
	private String details;

	public ExceptionResponse(Date timeStamp, String message, String details) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
