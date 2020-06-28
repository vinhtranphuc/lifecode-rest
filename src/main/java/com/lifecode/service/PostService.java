package com.lifecode.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.lifecode.common.BaseService;
import com.lifecode.common.Const;
import com.lifecode.common.FileUtil;
import com.lifecode.common.Utils;
import com.lifecode.jpa.repository.PostRepository;
import com.lifecode.jpa.repository.UserRepository;
import com.lifecode.mybatis.mapper.CategoryMapper;
import com.lifecode.mybatis.mapper.ImageMapper;
import com.lifecode.mybatis.mapper.PostMapper;
import com.lifecode.mybatis.mapper.TagMapper;
import com.lifecode.mybatis.mapper.UserMapper;
import com.lifecode.mybatis.model.CategoryVO;
import com.lifecode.mybatis.model.ImageVO;
import com.lifecode.mybatis.model.PostVO;
import com.lifecode.mybatis.model.TagVO;
import com.lifecode.mybatis.model.UserVO;
import com.lifecode.payload.PostRequest;

import javassist.NotFoundException;

@Service
public class PostService extends BaseService {
	
	private List<PostVO> posts;
	private List<TagVO> tags;
	private List<UserVO> users;
	private List<ImageVO> images;
	private CategoryVO category;

	@Resource private PostMapper postMapper;
	@Resource private UserMapper userMapper;
	@Resource private CategoryMapper categoryMapper;
	@Resource private TagMapper tagMapper;
	@Resource private ImageMapper imageMapper;

	@Autowired private PostRepository<?> postRepository;
	@Autowired private UserRepository userRepository;
	
	@Autowired SqlSessionFactory sqlSessionFactory;

	public List<PostVO> getPopularPosts() {
		posts = postMapper.selectPopularPosts();
		return getDetailPosts(posts);
	}

	public List<PostVO> getHotPosts() {
		posts = postMapper.selectHotPosts();
		return getDetailPosts(posts);
	}

	public List<PostVO> getRecentPosts() {
		posts = postMapper.selectRecentPosts();
		return getDetailPosts(posts);
	}
	
	public Map<String,Object> getPosts(Map<String, Object> param) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String page = (String) param.get("page");
		String recordsNo = (String) param.get("records_no");
		int pageInt = 0;
		int lastPage = 0;
		
		int totalPosts = postMapper.selectPostsTotCnt(param);
		result.put("total_posts", totalPosts);

		// get list of page
		if(Utils.isInteger(page) && Utils.isInteger(recordsNo)) {
			
			int recordsNoInt = Integer.parseInt(recordsNo);
			if(recordsNoInt == 0) {
				return result;
			}
			
			// int totalPosts = postMapper.selectPostsTotCnt(param);
			if(totalPosts < recordsNoInt) {
				posts = postMapper.selectPosts(param);
				result.put("page_of_post", 1);
				result.put("last_page", 1);
			} else {
				lastPage = totalPosts/recordsNoInt + ((totalPosts%recordsNoInt)>0?1:0);
				pageInt = Integer.parseInt(page);
				pageInt = pageInt<=0?lastPage:pageInt>lastPage?1:pageInt;
				
				int startPost = (pageInt-1)*recordsNoInt;
				param.put("start_post", startPost);
				posts = postMapper.selectPosts(param);
				
				result.put("page_of_post", pageInt);
				result.put("last_page", lastPage);
			}
		} else {
			posts =  postMapper.selectPosts(param);
			result.put("page_of_post", 1);
			result.put("last_page", 1);
		}
		
		result.put("list", getDetailPosts(posts));
		return result;
	}
	
	public List<PostVO> getOldPosts(Map<String, Object> param) {
		param.put("start_post", 1);
		posts = postMapper.selectOldPosts(param);
		return getDetailPosts(posts);
	}

	private List<PostVO> getDetailPosts(List<PostVO> posts) {
		if(posts.isEmpty()) {
			return new ArrayList<PostVO>();
		}
		for(PostVO e:posts) {
			getDetailPost(e);
		}
		return posts;
	}
	
	private PostVO getDetailPost(PostVO post){
		
		String content = StringUtils.isEmpty(post.getContent())?"":convertContentImgToUri(post.getContent());
		post.setContent(content);
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("post_id",post.getPost_id());
		
		List<CategoryVO> categories = categoryMapper.selectCategories(param);
		category = categories.size()>0?(CategoryVO) categories.get(0):null;
		post.setCategory(category);

		tags = tagMapper.selectTags(param);
		post.setTags(tags);
		
		users = userMapper.selectUsers(param);
		post.setUsers(users);
		
		images = FileUtil.convertPostImagesToUri(imageMapper.selectImages(param), severPost);

		post.setImages(images);
		
		return post;
	}

	private String convertContentImgToUri(String content) {
		
		Document doc = Jsoup.parse(content, "UTF-8");
		for (Element element : doc.select("img")) {
            String fileName = StringUtils.isEmpty(element.attr("src"))?"":element.attr("src");
            String fileUri = Const.getPostContentUri(Utils.getLocalIp()+":"+severPost,fileName);
        	element.attr("src", fileUri);
		}
		//return HtmlUtils.htmlEscape(doc.html());
		return doc.html();
	}

	public PostVO getPostById(String postId) throws NotFoundException {
		PostVO post = postMapper.getPostById(postId);
		if(post == null)
			throw new NotFoundException("This post ("+postId+") not exists !");
		return getDetailPost(post);
	}

	public Long createPost(PostRequest postReq) throws UnknownHostException {
		Long currentUserId = getCurrentUser().getId();
		postReq.user = userRepository.findById(currentUserId).get();
		return postRepository.save(postReq);
	}

	public PostVO editPost(PostRequest postReq) throws NotFoundException {
		Long currentUserId = getCurrentUser().getId();
		postReq.user = userRepository.findById(currentUserId).get();
		postRepository.update(postReq);
		return getPostById(postReq.postId+"");
	} 
	
	//@Transactional(propagation = Propagation.REQUIRED)
	@Transactional(value = "transactionManager", isolation = Isolation.READ_COMMITTED) //specific to roll back (throw Exception ...)
	public void deletePost(Long postId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("post_id", postId);

		// get related images
		List<Map<String, Object>> relatedImages = imageMapper.selectRelatedImagesByPostId(param);

		// check images of this post exists in another posts
		if (relatedImages.stream().anyMatch(t -> t.get("post_id") != postId)) {

			// delete posts_images
			param.put("isDeleteImg", "false");
			imageMapper.deletePostsImagesByPostId(param);
			
			// get images exists in another posts
			List<Long> anotherPostImages = relatedImages.stream().filter(t -> t.get("post_id") != postId)
					.map(t -> (Long) t.get("image_id")).collect(Collectors.toList());

			// delete image unused
			relatedImages.stream().filter(t -> t.get("post_id") == postId).forEach(t -> {

				// delete image of this post not exists in another post
				if (!anotherPostImages.contains(t.get("image_id"))) {
					param.put("image_id", t.get("image_id"));
					imageMapper.deleteImageByImageId(param);
					FileUtil.deleteImage(Const.IMG_POST_AVATAR_PATH, t.get("path") + "");
				}
			});
		} else {
			// delete all images by post id
			param.put("isDeleteImg", "true");
			imageMapper.deletePostsImagesByPostId(param);
			relatedImages.stream().forEach(t -> FileUtil.deleteImage(Const.IMG_POST_AVATAR_PATH, t.get("path") + ""));
		}

		// delete posts_tags
		tagMapper.deletePostsTagsByPostId(param);

		// delete posts_authors
		userMapper.deletePostsAuthorsByPostId(param);

		// delete post
		postMapper.deletePostByPostId(param);
	}
}
