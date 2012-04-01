package com.chihuo.bussiness;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// Generated 2012-2-14 22:19:35 by Hibernate Tools 3.4.0.CR1

/**
 * Table generated by hbm2java
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Desk implements java.io.Serializable {
	@XmlElement
	private Integer id;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private Integer capacity;

	public Desk() {
	}

	public Desk(String name, String description, Integer capacity) {
		this.name = name;
		this.description = description;
		this.capacity = capacity;
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

	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
}
