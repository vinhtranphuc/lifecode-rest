package com.lifecode.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import org.springframework.web.util.HtmlUtils;

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
import com.lifecode.utils.FileUtil;
import com.lifecode.utils.Utils;

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
		
		post.setContent(convertContentImgToUri(post.getContent()));
		
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
		return HtmlUtils.htmlEscape(doc.html());
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

	public PostVO getPostById(String postId) {
		PostVO post = postMapper.getPostById(postId);
		return getDetailPost(post);
	}

	public void createPost(PostRequest postReq) throws UnknownHostException {
		postReq.content = getConvertContent(postReq.content);
		postRepository.save(postReq);
	}

	private String getConvertContent(String content) {
		// update base64 img from content to url
		Document doc = Jsoup.parse(content, "UTF-8");
		int i = 0;
		for (Element element : doc.select("img")) {
			i++;
            String src = element.attr("src");
            if (src != null && src.startsWith("data:")) {
            	String fileName = FileUtil.saveBase64Image(src, Const.IMG_POST_CONTENT_PATH, Utils.getCurrentTimeStamp()+"_"+i);
            	element.attr("src", fileName);
            }
		}
		return doc.html();
	}
}
