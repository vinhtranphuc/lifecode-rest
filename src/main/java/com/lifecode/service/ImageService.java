package com.lifecode.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lifecode.common.BaseService;
import com.lifecode.common.Const;
import com.lifecode.common.FileUtil;
import com.lifecode.mybatis.mapper.ImageMapper;
import com.lifecode.mybatis.model.ImageVO;

@Service
public class ImageService extends BaseService {
	
	protected Logger logger = LoggerFactory.getLogger(ImageService.class);
	
	@Resource private ImageMapper imageMapper;

	public byte[] getImage(String subPath, String imageName) throws FileNotFoundException, IOException {

		String imagePath = null;
//		if(StringUtils.isEmpty(imageName) || !FilenameUtils.isExtension(imageName, Const.imgExtensions)) {
//			switch (subPath) {
//				case Const.USER_AVATAR_URI:
//					return getUserAvatarDefault();
//				default:
//					return getNotFoundImage();
//			}
//		}

		switch (subPath) {
			case Const.COMMON_URI:
				imagePath = Const.IMG_COMMON_PATH+"/"+imageName;
				break;
			case Const.USER_AVATAR_URI:
				imagePath = Const.IMG_USER_PATH+"/"+imageName;
				break;
			case Const.CATEGORY_URI:
				imagePath = Const.IMG_CATEGORY_PATH+"/"+imageName;
				break;
			case Const.POST_AVATAR_URI:
				imagePath = Const.IMG_POST_AVATAR_PATH+"/"+imageName;
				break;
			case Const.POST_CONTENT_URI:
				imagePath = Const.IMG_POST_CONTENT_PATH+"/"+imageName;
				break;
			default:
				return getNotFoundImage();
		}
		
		File file = new File(Const.UPLOAD_FOLDER_ROOT+"/"+imagePath);
		
		byte[] byteFile = null;
		try {
			byteFile = IOUtils.toByteArray(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException : {}", ExceptionUtils.getStackTrace(e));
			if(StringUtils.equals(subPath,Const.USER_AVATAR_URI)) {
				return getUserAvatarDefault();
			}
			return getNotFoundImage();
		} catch (IOException e) {
			logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
		}
	
	    return byteFile;
	}

	private byte[] getUserAvatarDefault() throws FileNotFoundException, IOException {
		File file = new File(Const.UPLOAD_FOLDER_ROOT+"/"+Const.IMG_USER_DEFAULT_DIR);
		byte[] byteFile = IOUtils.toByteArray(new FileInputStream(file));
	    return byteFile;
	}

	private byte[] getNotFoundImage() throws FileNotFoundException, IOException {
		File file = new File(Const.UPLOAD_FOLDER_ROOT+"/"+Const.IMG_NOT_FOUND_DIR);
		byte[] byteFile = IOUtils.toByteArray(new FileInputStream(file));
	    return byteFile;
	}

	public List<String> getUriImages(Map<String, Object> param) {
		List<ImageVO> images = FileUtil.convertPostImagesToUri(imageMapper.selectImages(param), severPost);
		List<String> uriImages = images.stream().filter(t -> t instanceof ImageVO)
				.map(t -> ((ImageVO) t).getFile_name()).collect(Collectors.toList());
		return uriImages;
	}
}
