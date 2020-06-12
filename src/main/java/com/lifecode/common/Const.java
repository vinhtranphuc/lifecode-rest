package com.lifecode.common;

public class Const {
	
	// pagination
    public static final  String DEFAULT_PAGE_NUMBER = "0";
    public static final  String DEFAULT_PAGE_SIZE = "30";
    public static int MAX_PAGE_SIZE = 50;

    // file
    public static final String UPLOAD_FOLDER_ROOT = "upload";
    public static final String IMG_COMMON_PATH = "images/common";
    public static final String IMG_NOT_FOUND_NAME = "not-found";
    public static final String IMG_NOT_FOUND_DIR = IMG_COMMON_PATH+"/"+IMG_NOT_FOUND_NAME+".png";
    public static final String DEFAULT_IMG_TYPE = ".png";

    public static final String IMG_CATEGORY_PATH = "images/category";
    public static final String IMG_POST_CONTENT_PATH = "images/post/content";
    public static final String IMG_POST_FEATURES_PATH = "images/post/features";
    
    //uri
    public static final String HTTP = "http://";
    public static final String API_IMG_URI = "api/image";
    public static final String VIEW_URI = "view";
    public static final String COMMON_URI = "common";
    public static final String CATEGORY_URI = "category";
    public static final String POST_CONTENT_URI = "post-content";
    public static final String POST_FEATURES_URI = "post-features";

    public static final String[] imgExtensions = {"png","jpg"};
    
    public static final String getImgCategoryUri(String host,String fileName) {
    	return HTTP+host+"/"+API_IMG_URI+"/"+VIEW_URI+"/"+IMG_CATEGORY_PATH+"/"+fileName;
    }

    public static final String getPostContentUri(String host,String fileName) {
    	return HTTP+host+"/"+API_IMG_URI+"/"+VIEW_URI+"/"+POST_CONTENT_URI+"/"+fileName;
    }

    public static final String getPostFeaturesUri(String host,String fileName) {
    	return HTTP+host+"/"+API_IMG_URI+"/"+VIEW_URI+"/"+POST_FEATURES_URI+"/"+fileName;
    }
}
