package com.lifecode.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lifecode.service.ImageService;

@RestController
@RequestMapping(value = "api/image")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class ImageController {
	
	protected Logger logger = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private ImageService imageService;

	@RequestMapping(value = "/view/{subPath}/{imageName:.+}", method = RequestMethod.GET)
	public @ResponseBody byte[] getImage(@PathVariable(value = "subPath") String subPath,
			@PathVariable(value = "imageName") String imageName) throws IOException {
		return imageService.getImage(subPath,imageName);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/preview")
	public <T> ResponseEntity<T> preview(HttpServletResponse response, MultipartHttpServletRequest mRequest) {
		try {
			
			List<MultipartFile> multipartFiles = mRequest.getMultiFileMap().get("files");
			List<String> resultList = new ArrayList<String>();
			StringBuilder sb;
		    for (MultipartFile multipartFile: multipartFiles) {
		    	byte[] byteArr = multipartFile.getBytes();
		    	sb = new StringBuilder();
		    	sb.append("data:image/png;base64,");
		    	sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(byteArr, false)));
		    	resultList.add(sb.toString());
		    }
			response.addHeader("Access-Control-Allow-Credentials", "true");
			return (ResponseEntity<T>) ResponseEntity.ok().body(resultList);
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
