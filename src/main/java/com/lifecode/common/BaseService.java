package com.lifecode.common;

import org.springframework.beans.factory.annotation.Value;

public class BaseService {
	
	@Value("${server.port}")
	protected String severPost;
}
