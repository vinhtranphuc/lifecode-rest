package com.lifecode.common;

import org.springframework.beans.factory.annotation.Value;

public class BaseController {

	@Value("${server.port}")
	protected String severPost;
}
