package com.lifecode.payload;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostRequest {
	
	public Long postId;
	
	@NotNull(message="Category is required !")
	public Long categoryId;
	
	@NotEmpty(message="Tag is required !")
	public List<Long> tags;
	
	@NotNull(message = "Level is required !")
	@Min(value = 1,message = "Level must be from 1 to 5 image !")
	@Max(5)
	public int level;
	
	@NotEmpty(message="Post images is required !")
	@Size(min=1, max=5, message = "Post images must be from 1 to 5 image !")
	public List<String> postImages;
	
	@NotEmpty(message = "Level is required !")
	public String title;
	
	@NotEmpty(message = "Content is required !")
	public String content;
}
