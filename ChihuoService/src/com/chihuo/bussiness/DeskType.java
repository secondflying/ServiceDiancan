package com.chihuo.bussiness;

// Generated 2012-5-4 12:04:57 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeskType implements java.io.Serializable {
	@XmlElement
	private Integer id;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlTransient
	private Set<Desk> desks = new HashSet<Desk>(0);

	public DeskType() {
	}

	public DeskType(String name, String description, Set<Desk> desks) {
		this.name = name;
		this.description = description;
		this.desks = desks;
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

	public Set<Desk> getDesks() {
		return this.desks;
	}

	public void setDesks(Set<Desk> desks) {
		this.desks = desks;
	}

}
