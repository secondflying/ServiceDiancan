package com.chihuo.bussiness;


import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category implements java.io.Serializable {
	@XmlElement
	private Integer id;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private String image;

	@XmlTransient
	private Set<Recipe> recipes = new HashSet<Recipe>(0);

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, String description, Set<Recipe> recipes) {
		this.name = name;
		this.description = description;
		this.recipes = recipes;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Recipe> getRecipes() {
		return this.recipes;
	}

	public void setRecipes(Set<Recipe> recipes) {
		this.recipes = recipes;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
