package com.lifecode.payload;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class Response {
	
	private int status;
	public Date timestamp;
	public Object data;
	public String message;
	public List<String> messages;
	private String accessToken;

	public Response(Object data) {
		this.timestamp = new Date();
		this.data = data;
	}
	
	public Response(Object data, String message) {
		this.timestamp = new Date();
		this.data = data;
		this.message = message;
	}
	
	public Response(Object data, String message, String accessToken) {
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
		if(StringUtils.isEmpty(this.message)) {
			this.message = status==HttpStatus.OK.value()?"Sucessfully":"An error occurred !";
		}
		this.status = status;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}