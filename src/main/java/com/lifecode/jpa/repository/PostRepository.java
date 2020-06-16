package com.lifecode.jpa.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lifecode.common.Const;
import com.lifecode.jpa.entity.Category;
import com.lifecode.jpa.entity.Image;
import com.lifecode.jpa.entity.Post;
import com.lifecode.jpa.entity.Tag;
import com.lifecode.payload.PostRequest;
import com.lifecode.utils.FileUtil;
import com.lifecode.utils.Utils;

@Repository
public interface PostRepository<T> extends JpaRepository<Post, Long>, PostRepositoryCustom<T> {

	boolean existsByCategoryId(Long categoryId);
	
	@Query("select p.content from Post p where p.id = :id")
	String findContentById(@Param("id") Long id);
}

interface PostRepositoryCustom<T> {
	void refresh(T t);

	Long save(PostRequest postReq);

	void update(PostRequest postReq);
}

@Repository(value = "PostRepositoryImpl")
@Transactional(rollbackFor = Exception.class)
class PostRepositoryImpl<T> implements PostRepositoryCustom<T> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	CategoryRepository categoryRepository;

	private Session session;

	@Override
	@Transactional
	public void refresh(Object entity) {
		entityManager.refresh(entity);
	}

	public Long save(PostRequest postReq) {

		session = entityManager.unwrap(Session.class);

		List<Tag> tags = session.createQuery("select t from Tag t where t.id in :tags", Tag.class)
				.setParameter("tags", postReq.tags).getResultList();

		Category category = session.find(Category.class, postReq.categoryId);
		List<Image> postImages = savePostImages(postReq.postImages);
		Post post = new Post(category, postImages, tags, postReq.level, postReq.title, postReq.content, 0);

		session.save(post);
		session.flush();

		return post.getId();
	}

	private List<Image> savePostImages(List<String> postImages) {
		List<Image> images = new ArrayList<Image>();
		int i = 0;
		Image image;
		for (String e : postImages) {
			i++;
			String fileName = FileUtil.saveBase64Image(e, Const.IMG_POST_FEATURES_PATH,
					Utils.getCurrentTimeStamp() + "_" + i);
			image = new Image(fileName);
			session.save(image);
			// this.refresh(image);
			images.add(image);
		}
		return images;
	}

	@Override
	public void update(PostRequest postReq) {
		session = entityManager.unwrap(Session.class);
		
		Post post = session.get(Post.class, postReq.postId);
		
		List<String> oldPostImages = post.getImages().stream().map(t -> t.getPath()).collect(Collectors.toList());
		List<Image> editPostImages = editPostImages(postReq.postImages, oldPostImages);
		List<Tag> tags = session.createQuery("select t from Tag t where t.id in :tags", Tag.class)
				.setParameter("tags", postReq.tags).getResultList();
		Category category = session.get(Category.class, postReq.categoryId);
		
		post.setImages(new HashSet<>(editPostImages));
		post.setTags(new HashSet<>(tags));
		post.setCategory(category);
		post.setLevel(postReq.level);
		post.setTitle(postReq.title);
		post.setContent(postReq.content);
		
		session.evict(post);
		session.update(post);
		
		deleteFileNotUse(editPostImages,oldPostImages);
		session.flush();
		//this.refresh(post);
	}

	private List<Image> editPostImages(List<String> postImages, List<String> oldPostImages) {
		List<String> notEditFiles = new ArrayList<String>();
		List<Image> images = new ArrayList<Image>();
		int i = 0;
		Image image;
		for (String e : postImages) {
			i++;
			if (e != null && e.startsWith("data:")) {
				String fileName = FileUtil.saveBase64Image(e, Const.IMG_POST_FEATURES_PATH,
						Utils.getCurrentTimeStamp() + "_" + i);
				image = new Image(fileName);
				session.save(image);
				// this.refresh(image);
				images.add(image);
			} else {
				String notEditFile = e.substring(e.lastIndexOf("/") + 1);
				notEditFiles.add(notEditFile);
			}
		}

		if(notEditFiles.size() > 0) {
			List<Image> notEditImages = session.createQuery("select i from Image i where i.path in :images", Image.class)
					.setParameter("images", notEditFiles).getResultList();
			images.addAll(notEditImages);
		}

		return images;
	}
	
	private void deleteFileNotUse(List<Image> images, List<String> oldImages) {
		
		List<String> newImages = images.stream().map(t->t.getPath()).collect(Collectors.toList());
		
		// get file not use
		List<String> deleteFiles = new ArrayList<String>(oldImages);
		deleteFiles.removeAll(newImages);

		// delete file not use
		for (String fileName : deleteFiles) {
			FileUtil.deleteImage(Const.IMG_POST_FEATURES_PATH, fileName);
			Image delImage = session.createQuery("select i from Image i where i.path =:fileName", Image.class)
					.setParameter("fileName", fileName).getSingleResult();
			session.remove(delImage);
		}
	}
}