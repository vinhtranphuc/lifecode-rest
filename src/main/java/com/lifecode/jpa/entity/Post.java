package com.lifecode.jpa.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.lifecode.jpa.entity.audit.DateAudit;

@Entity
@Table(name = "posts")
public class Post extends DateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3110411846914329919L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(
			mappedBy = "post",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	private Set<Comment> comments;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "posts_images", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "image_id"))
	private Set<Image> images = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "posts_tags", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "posts_authors", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();

	public int level;
	
	@Lob
	@Length(max = 65535)
	private String title;
	
	@Lob
	@Length(max = 65535)
	private String sumary;

	@Lob
	@Length(max = 65535)
	private String content;
	
	public int times_of_view;

	public Long getId() {
		return id;
	}
	
	public Post() {
		super();
	}

	public Post(Category category, List<Image> images, List<Tag> tags, int level,String title,String content, int time_of_view) {
		super();
		this.category = category;
		this.images = new HashSet<>(images);
		this.tags = new HashSet<>(tags);
		this.level = level;
		this.title = title;
		this.content = content;
		this.times_of_view = time_of_view;
	}
}
