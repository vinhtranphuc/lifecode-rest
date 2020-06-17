package com.lifecode.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lifecode.common.Const;
import com.lifecode.jpa.repository.PostRepository;
import com.lifecode.mybatis.mapper.ImageMapper;
import com.lifecode.mybatis.mapper.PostMapper;
import com.lifecode.mybatis.mapper.TagMapper;
import com.lifecode.mybatis.mapper.UserMapper;
import com.lifecode.mybatis.model.ImageVO;
import com.lifecode.mybatis.model.PostVO;
import com.lifecode.mybatis.model.TagVO;
import com.lifecode.mybatis.model.UserVO;
import com.lifecode.payload.PostRequest;
import com.lifecode.utils.Utils;

import javassist.NotFoundException;

@Service
public class PostService {
	
	protected Logger logger = LoggerFactory.getLogger(PostService.class);
	
	private String localIp;
	
	@Value("${server.port}")
	private String severPost;
	
	private List<PostVO> list;
	
	private List<TagVO> tags;
	
	private List<UserVO> users;
	
	private List<ImageVO> images;

	@Resource private PostMapper postMapper;
	@Resource private UserMapper userMapper;
	@Resource private TagMapper tagMapper;
	@Resource private ImageMapper imageMapper;
	
	@Autowired private PostRepository<?> postRepository;
	
	public List<PostVO> getPopularPosts() {
		list = postMapper.selectPopularPosts();
		return getDetailPosts(list);
	}

	public List<PostVO> getHotPosts() {
		list = postMapper.selectHotPosts();
		return getDetailPosts(list);
	}

	public List<PostVO> getRecentPosts() {
		list = postMapper.selectRecentPosts();
		return getDetailPosts(list);
	}
	
	public Map<String,Object> getPosts(Map<String, Object> param) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String page = (String) param.get("page");
		String recordsNo = (String) param.get("records_no");
		int pageInt = 0;
		int lastPage = 0;

		// get list of page
		if(Utils.isInteger(page) && Utils.isInteger(recordsNo)) {
			
			int recordsNoInt = Integer.parseInt(recordsNo);
			if(recordsNoInt == 0) {
				return result;
			}
			
			int totalPosts = postMapper.selectPostsTotCnt(param);
			if(totalPosts < recordsNoInt) {
				list = postMapper.selectPosts(param);
			} else {
				lastPage = totalPosts/recordsNoInt + ((totalPosts%recordsNoInt)>0?1:0);
				pageInt = Integer.parseInt(page);
				pageInt = pageInt<=0?lastPage:pageInt>lastPage?1:pageInt;
				
				int startPost = (pageInt-1)*recordsNoInt;
				param.put("start_post", startPost);
				list = postMapper.selectPosts(param);
				
				result.put("page_of_post", pageInt);
				result.put("last_page", lastPage);
			}
		} else {
			list =  postMapper.selectPosts(param);
			result.put("page", 1);
			result.put("last_page", 1);
		}
		
		result.put("list", getDetailPosts(list));
		return result;
	}
	
	public List<PostVO> getOldPosts(Map<String, Object> param) {
		param.put("start_post", 1);
		list = postMapper.selectOldPosts(param);
		return getDetailPosts(list);
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
		
		tags = tagMapper.selectTags(param);
		post.setTags(tags);
		
		users = userMapper.selectUsers(param);
		post.setUsers(users);
		
		images = convertPostImagesToUri(imageMapper.selectImages(param));

		post.setImages(images);
		
		return post;
	}

	private String convertContentImgToUri(String content) {
		
		try {
			localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException : {}", ExceptionUtils.getStackTrace(e));
		}
		
		Document doc = Jsoup.parse(content, "UTF-8");
		for (Element element : doc.select("img")) {
            String fileName = StringUtils.isEmpty(element.attr("src"))?"":element.attr("src");
            String fileUri = Const.getPostContentUri(localIp+":"+severPost,fileName);
        	element.attr("src", fileUri);
		}
		//return HtmlUtils.htmlEscape(doc.html());
		return doc.html();
	}
	
	private List<ImageVO> convertPostImagesToUri(List<ImageVO> postImages) {
		try {
			localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException : {}", ExceptionUtils.getStackTrace(e));
		}
		
		for(ImageVO img:postImages) {
			String fileUri = Const.getPostFeaturesUri(localIp+":"+severPost,img.getPath());
			img.setPath(fileUri);
		}
		return postImages;
	}

	public PostVO getPostById(String postId) throws NotFoundException {
		PostVO post = postMapper.getPostById(postId);
		if(post == null)
			throw new NotFoundException("This post ("+postId+") not exists !");
		return getDetailPost(post);
	}

	public Long createPost(PostRequest postReq) throws UnknownHostException {
		return postRepository.save(postReq);
	}

	public PostVO editPost(@Valid PostRequest postReq) throws NotFoundException {
		postRepository.update(postReq);
		return getPostById(postReq.postId+"");
	}
}
