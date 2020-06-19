package com.lifecode.payload;

import javax.validation.constraints.NotNull;

public class CategoryRequest {
	
	@NotNull(message="Category name is required !")
	public String category;
	
	@NotNull(message="Category image is required !")
	public String categoryImg;
}
