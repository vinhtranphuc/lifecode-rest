package com.lifecode.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.lifecode.payload.Response;

@ControllerAdvice(basePackages = { "com.lifecode.controller" })   // package where it will look for the controllers.
public class ResponseFilter implements ResponseBodyAdvice<Object>  {

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
			ServletServerHttpResponse sshr = (ServletServerHttpResponse) response;
			HttpServletResponse hsr = sshr.getServletResponse();
			System.out.println(hsr.getStatus());
		
		System.out.println(body);
	//	(com.lifecode.payload.Response) body).status = hsr.getStatus();

		return response;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
	//	returnType.getGenericParameterType().
		return returnType.getGenericParameterType() == Response.class;
	}
}