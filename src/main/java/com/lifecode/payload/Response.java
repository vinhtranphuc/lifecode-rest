package com.lifecode.payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class Response {
	
	private int status;
	public Date timestamp;
	public Object data;
	private String message;
	public List<String> messages;
	private String accessToken;
	
	public Response() {
		this.timestamp = new Date();
	}
	
	public Response(Object data) {
		this.timestamp = new Date();
		this.data = data;
	}
	
	public Response(Object data, String message) {
		this.timestamp = new Date();
		this.data = data;
		this.message = message;
		messages = new ArrayList<String>();
		messages.add(message);
	}
	
	public Response(HttpStatus status, List<String> messages) {
		this.timestamp = new Date();
		this.status = status.value();
		this.messages = messages;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		messages = new ArrayList<String>();
		messages.add(message);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		if(StringUtils.isEmpty(this.message)) {
			this.message = status==HttpStatus.OK.value()?"Sucessfully":"An server error has occurred!";
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