package com.lifecode.jpa.entity;

import java.util.HashSet;
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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "images")
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;
	
	@Size(max = 500)
	private String path;
	
	@ManyToMany(cascade= {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "posts_images", joinColumns = @JoinColumn(name = "image_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
	public Set<Post> posts = new HashSet<>();

	public Image() {
		super();
	}
	
	public Image(String path) {
		super();
		this.path = path;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
