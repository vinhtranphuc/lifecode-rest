package com.lifecode.payload;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostRequest {
	
	@NotEmpty(message="Category is required !")
	public String categoryId;
	
	@NotEmpty(message="Tag is required !")
	public List<String> tags;
	
	@NotNull(message = "Level is required !")
	public int level;
	
	@NotEmpty(message="Post images is required !")
	@Size(min=3, max=5, message = "Post images must be from 3 to 5 image !")
	public List<String> postImages;
	
	@NotEmpty(message = "Level is required !")
	public String title;
	
	@NotEmpty(message = "Content is required !")
	public String content;
}
