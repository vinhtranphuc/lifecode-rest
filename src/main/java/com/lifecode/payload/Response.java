package com.lifecode.payload;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

public class Response {
	
	private int status;
	public Date timestamp;
	public Object data;
	public String message;
	public List<String> messages;
	public String accessToken;
	
	public Response(HttpStatus status) {
		this.timestamp = new Date();
		this.status = status.value();
		this.data = null;
		this.message = status==HttpStatus.OK?"Sucessfully":"An error occurred !";
		this.accessToken = null;
	}
	
	public Response(String message) {
		this.timestamp = new Date();
		this.message = message;
	}

	public Response(HttpStatus status, Object data) {
		this.timestamp = new Date();
		this.status = status.value();
		this.data = data;
		this.message = status==HttpStatus.OK?"Sucessfully":"An error occurred !";
	}
	
	public Response(HttpStatus status, Object data, String message) {
		this.timestamp = new Date();
		this.status = status.value();
		this.data = data;
		this.message = message;
	}
	
	public Response(HttpStatus status, Object data, String message, String accessToken) {
		this.status = status.value();
		this.data = data;
		this.message = message;
		this.accessToken = accessToken;
	}

	public Response(HttpStatus status, List<String> messages) {
		this.timestamp = new Date();
		this.status = status.value();
		this.messages = messages;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}