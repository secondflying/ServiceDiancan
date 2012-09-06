package com.chihuo.bussiness;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={"id","name","price","description","image","cid","cname"})
public class Recipe implements java.io.Serializable {
	private Integer id;
	private Category category;
	private String name;
	private String description;
	private String image;
	private Integer price;
	private Integer isdelete;

	public Recipe() {
	}

	public Recipe(Category category, String name, String description,
			String image, Integer price) {
		this.category = category;
		this.name = name;
		this.description = description;
		this.image = image;
		this.price = price;
	}

	@XmlElement
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@XmlElement
	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@XmlElement
	public Integer getCid(){
		if (this.category != null) {
			return this.category.getId();
		}
		return null;
	}

	@XmlElement
	public String getCname(){
		if (this.category != null) {
			return this.category.getName();
		}
		return null;
	}

	public Integer getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
	}

}
