package com.lifecode.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifecode.mybatis.model.ImageVO;

public class FileUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static String saveBase64Image(String encodedString, String subPath, String fileName) {
		try {
			String partSeparator = ",";
			if (encodedString.contains(partSeparator)) {
			  String encodedImg = encodedString.split(partSeparator)[1];
			  byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
			  if(!FilenameUtils.isExtension(fileName, Const.imgExtensions)) {
				  fileName = fileName+Const.DEFAULT_IMG_TYPE;
			  }
			  Path folderPath = Paths.get(Const.UPLOAD_FOLDER_ROOT +"/"+subPath);
			  if (!Files.exists(folderPath)) {
				  folderPath = Files.createDirectory(folderPath);
			  }
			  Path filePath = Paths.get(folderPath+"/"+fileName);
			  Files.write(filePath, decodedImg);
			  return fileName;
			}
		} catch (IOException e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public static void deleteImage(String subPath, String fileName) {
		try {
			 Path path = Paths.get(Const.UPLOAD_FOLDER_ROOT +"/"+subPath+"/"+fileName);
			 if(Files.exists(path))
				 Files.delete(path);
		} catch (IOException e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static List<ImageVO> convertPostImagesToUri(List<ImageVO> postImages, String serverPost) {
		for(ImageVO img:postImages) {
			String fileUri = Const.getPostAvatarUri(Utils.getLocalIp()+":"+serverPost,img.getPath());
			img.setPath(fileUri);
		}
		return postImages;
	}
}
