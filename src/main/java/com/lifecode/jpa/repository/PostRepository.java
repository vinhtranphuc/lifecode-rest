package com.lifecode.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface PostRepository<T> extends JpaRepository<Post, Long>, PostRepositoryCustom<T>{
}
interface PostRepositoryCustom<T> {
	void refresh(T t);
	void save(PostRequest postReq);
}
@Repository(value = "PostRepositoryImpl")
@Transactional(rollbackFor = Exception.class)
class PostRepositoryImpl<T> implements PostRepositoryCustom<T> {

	@PersistenceContext
    private EntityManager entityManager;
	
	private Session session;

	@Override
    @Transactional
	public void refresh(Object entity) {
		entityManager.refresh(entity);
	}

	@Override
	public void save(PostRequest postReq) {

		session = entityManager.unwrap(Session.class);
		
		List<Tag> tags= session.createQuery("select t from Tag t where t.id in :tags",Tag.class)
				.setParameter("tags", postReq.tags).getResultList();
		
		Category category = session.find(Category.class, postReq.categoryId);
		List<Image> postImages = savePostImages(postReq.postImages);
		Post post = new Post(category,postImages, tags,postReq.level,postReq.title, postReq.content, 0);
		
		session.save(post);
		session.flush();
	}

	private List<Image> savePostImages(List<String> postImages) {
		List<Image> images = new ArrayList<Image>();
		int i = 0;
		Image image;
		for(String e:postImages) {
			i++;
			String fileName = FileUtil.saveBase64Image(e, Const.IMG_POST_FEATURES_PATH, Utils.getCurrentTimeStamp()+"_"+i);
			image = new Image(fileName);
			session.save(image);
			//this.refresh(image);
			images.add(image);
		}
		return images;
	}
}