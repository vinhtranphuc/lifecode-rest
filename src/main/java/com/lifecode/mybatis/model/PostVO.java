package com.lifecode.mybatis.model;

import java.util.List;

public class PostVO {
	
	private String post_id;
	private String category_id;
	private String post_image;
	private String level;
	private String title;
	private String sumary;
	private String content;
	private String created_at;
	private String times_of_view;
	private String number_of_comments;
	
	private List<TagVO> tags;
	private List<UserVO> userVOs;
	private List<ImageVO> images;
	
	public String getPost_id() {
		return post_id;
	}
	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getPost_image() {
		return post_image;
	}
	public void setPost_image(String post_image) {
		this.post_image = post_image;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSumary() {
		return sumary;
	}
	public void setSumary(String sumary) {
		this.sumary = sumary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getTimes_of_view() {
		return times_of_view;
	}
	public void setTimes_of_view(String times_of_view) {
		this.times_of_view = times_of_view;
	}
	public List<TagVO> getTags() {
		return tags;
	}
	public void setTags(List<TagVO> tags) {
		this.tags = tags;
	}
	public List<UserVO> getUsers() {
		return userVOs;
	}
	public void setUsers(List<UserVO> userVOs) {
		this.userVOs = userVOs;
	}
	public List<ImageVO> getImages() {
		return images;
	}
	public void setImages(List<ImageVO> images) {
		this.images = images;
	}
	public String getNumber_of_comments() {
		return number_of_comments;
	}
	public void setNumber_of_comments(String number_of_comments) {
		this.number_of_comments = number_of_comments;
	}
}