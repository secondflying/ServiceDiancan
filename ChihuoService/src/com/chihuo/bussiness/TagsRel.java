package com.chihuo.bussiness;

// Generated 2012-2-14 22:19:35 by Hibernate Tools 3.4.0.CR1

/**
 * TagsRel generated by hbm2java
 */
public class TagsRel implements java.io.Serializable {

	private Integer id;
	private Tags tags;
	private int iit;

	public TagsRel() {
	}

	public TagsRel(Tags tags, int iit) {
		this.tags = tags;
		this.iit = iit;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Tags getTags() {
		return this.tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public int getIit() {
		return this.iit;
	}

	public void setIit(int iit) {
		this.iit = iit;
	}

}