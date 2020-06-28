package com.lifecode.common;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MybatisUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(MybatisUtils.class);
	
	/**
	 * parameter null ex. <if test="@com.lifecode.common.MybatisUtils@isEmpty(parameter)">
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) throws IllegalArgumentException {
		if (obj == null) return true;
		if (obj instanceof String && ((String) obj).length() == 0) return true;
		else if (obj instanceof Collection && ((Collection) obj).isEmpty()) return true;
		else if (obj.getClass().isArray() && Array.getLength(obj) == 0) return true;
		else if (obj instanceof Map && ((Map) obj).isEmpty()) return true;
		else return false;
	}

	/**
	 * parameter not null ex. <if test="@com.lifecode.common.MybatisUtils@isNotEmpty(parameter)">
	 */
	public boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
	
	/**
	 * parameter is collection and not null ex. <if test="@com.lifecode.common.MybatisUtils@isExistCollection(parameter)">
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isExistCollection(Object obj) {
		if (obj instanceof Collection && !((Collection) obj).isEmpty()) return true;
		return false;
	}
	
	/**
	 * parameter is collection and not null ex. <if test="@com.lifecode.common.MybatisUtils@isValidFormat(parameter)">
	 */
	public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
        	// logger.error("Excecption : {}", ExceptionUtils.getStackTrace(ex));
        }
        return date != null;
    }
}
