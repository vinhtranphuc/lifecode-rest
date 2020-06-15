package com.lifecode.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifecode.exception.BusinessException;
import com.lifecode.jpa.entity.Category;
import com.lifecode.jpa.entity.Tag;
import com.lifecode.mybatis.model.CategoryVO;
import com.lifecode.mybatis.model.PostVO;
import com.lifecode.mybatis.model.TagVO;
import com.lifecode.payload.PostRequest;
import com.lifecode.payload.Response;
import com.lifecode.service.CategoryService;
import com.lifecode.service.PostService;
import com.lifecode.service.TagService;

import javassist.NotFoundException;

@RestController
@RequestMapping(value = "api/blog")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","http://127.0.0.1:3000","http://127.0.0.1:3001"})
public class BlogController {

	protected Logger logger = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	private PostService postService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TagService tagService;
	
	@RequestMapping(value = "/hot-posts", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getHotPosts() {
		try {
			List<PostVO> result = postService.getHotPosts();
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value = "/recent-posts", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getRecentPosts() {

		try {
			List<PostVO> result = postService.getRecentPosts();
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value = "/popular-posts", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getPopularPosts() {

		try {
			List<PostVO> result = postService.getPopularPosts();
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.badRequest().body(new Response(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@RequestMapping(value = "/old-posts", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getOldPosts(@RequestParam Map<String,Object> param) {

		try {
			List<PostVO> result = postService.getOldPosts(param);
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value = "/posts", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getPosts(@RequestParam Map<String,Object> param) {

		try {
			Map<String,Object> result = postService.getPosts(param);
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value = "/post/{post_id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getPostById(@PathVariable(value = "post_id") String postId) {

		try {
			PostVO result = postService.getPostById(postId);
			return ResponseEntity.ok().body(new Response(result));
		} catch(NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(null,e.getMessage()));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.badRequest().body(new Response(HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getCategories(@RequestParam Map<String,Object> param) {

		try {
			List<CategoryVO> result = categoryService.getCategories(param);
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value  = "/tags", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> getTagsByPostId(@RequestParam Map<String,Object> param) {

		try {
			List<TagVO> result = tagService.getTags(param);
			return ResponseEntity.ok().body(new Response(result));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PostMapping("/add-tag")
	public ResponseEntity<Response> addTag(@RequestBody Map<String,Object> param) {
		
		try {
			Tag tag = tagService.addTag(param);
			return ResponseEntity.ok().body(new Response(tag,"You're successfully add tag."));
		} catch(BusinessException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(null,e.getMessage()));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/remove-tag")
	public ResponseEntity<Response> removeTag(@RequestBody Map<String,Object> param) {
		try {
			List<Integer> list = (List<Integer>) param.get("tagIds");
			tagService.removeTag(list);
			return ResponseEntity.ok().body(new Response(null,"You're successfully remove tag."));
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(null,"Existing tags are being used in other posts"));
			}
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(value  = "/check-exists-category", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> checkExistsCategory(@RequestParam(value="category") String category) {

		try {
			Boolean isExists = categoryService.isExistsCategory(category);
			return ResponseEntity.ok().body(new Response(isExists));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@PostMapping("/add-category")
	public ResponseEntity<Response> addCategory(@RequestBody Map<String,Object> param) {
		
		try {
			String message = "";
			String categoryName = (String) param.get("category");
			String base64Img = (String) param.get("categoryImg"); 
			
			if(StringUtils.isEmpty(categoryName)) {
				message = "Please input category name";
			} else if(StringUtils.isEmpty(base64Img)) {
				message = "Please select category image";
			}

			if(StringUtils.isNotEmpty(message)) {
				return ResponseEntity.ok().body(new Response(null,message));
			}
			
			Category category = categoryService.saveCategory(categoryName,base64Img);
			return ResponseEntity.ok().body(new Response(category,"You're successfully add category."));
		} catch(BusinessException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(null,e.getMessage()));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@RequestMapping(value  = "/remove-category", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Response> removeCategory(@RequestParam(value="categoryId") Long categoryId) {
		try {
			categoryService.removeCategory(categoryId);
			return ResponseEntity.ok().body(new Response(null,"You're successfully remove category."));
		} catch(BusinessException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(null,e.getMessage()));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@PostMapping("/create-post")
	public ResponseEntity<Response> createPost(@Valid @RequestBody PostRequest post) {
		try {
			Long postId = postService.createPost(post);
			return ResponseEntity.ok().body(new Response(postId,"You're successfully create post."));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@PostMapping("/edit-post")
	public ResponseEntity<Response> editPost(@Valid @RequestBody PostRequest post) {
		try {
			Long postId = postService.editPost(post);
			return ResponseEntity.ok().body(new Response(postId,"You're successfully create post."));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
