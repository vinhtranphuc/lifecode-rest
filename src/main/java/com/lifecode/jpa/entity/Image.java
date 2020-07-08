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
	
	@Size(max = 30)
	private String file_name;
	
	@ManyToMany(cascade= {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "posts_images", joinColumns = @JoinColumn(name = "image_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
	public Set<Post> posts = new HashSet<>();

	public Image() {
		super();
	}
	
	public Image(String file_name) {
		super();
		this.file_name = file_name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}
