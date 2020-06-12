package com.lifecode.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.lifecode.payload.Response;
import com.lifecode.security.JwtTokenProvider;

@ControllerAdvice(basePackages = { "com.lifecode.controller" })   // package where it will look for the controllers.
public class ResponseFilter<T> implements ResponseBodyAdvice<Object>  {
	
	@Autowired
	JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	 
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		// get status from response
		ServletServerHttpResponse sshrs = (ServletServerHttpResponse) response;
		HttpServletResponse hsrs = sshrs.getServletResponse();
		int status = hsrs.getStatus();
		
		if(status == HttpStatus.INTERNAL_SERVER_ERROR.value() && body == null) {
			Response resBody = new Response();
			resBody.setMessage("An server error has occurred");
			return resBody;
		}

		if(body==null || body.getClass() != Response.class)
			return body;
		
		if(status == HttpStatus.OK.value()) {

			// get current jwt from request
			ServletServerHttpRequest sshrq = (ServletServerHttpRequest) request;
			HttpServletRequest hsrq = sshrq.getServletRequest();
			String jwt = tokenProvider.getJwtFromRequest(hsrq);
			
			//save to Response
			((Response) body).setAccessToken(jwt);
		}

		// set status to body
		((Response) body).setStatus(hsrs.getStatus());

		return body;
	}
}