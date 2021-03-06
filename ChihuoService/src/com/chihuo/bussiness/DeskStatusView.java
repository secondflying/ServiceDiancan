package com.chihuo.bussiness;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// Generated 2012-5-2 22:06:47 by Hibernate Tools 3.4.0.CR1

/**
 * DeksStatusViewId generated by hbm2java
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeskStatusView implements java.io.Serializable {

	@XmlElement
	private int id;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private Integer capacity;
	@XmlElement
	private Integer tid;
	@XmlElement
	private Integer oid;
	@XmlElement
	private Integer status;
	@XmlElement
	private String code;
	
	public DeskStatusView() {
	}

	public DeskStatusView(int id) {
		this.id = id;
	}

	public DeskStatusView(int id, String name, String description,
			Integer capacity, Integer status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.status = status;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DeskStatusView))
			return false;
		DeskStatusView castOther = (DeskStatusView) other;

		return (this.getId() == castOther.getId())
				&& ((this.getName() == castOther.getName()) || (this.getName() != null
						&& castOther.getName() != null && this.getName()
						.equals(castOther.getName())))
				&& ((this.getDescription() == castOther.getDescription()) || (this
						.getDescription() != null
						&& castOther.getDescription() != null && this
						.getDescription().equals(castOther.getDescription())))
				&& ((this.getCapacity() == castOther.getCapacity()) || (this
						.getCapacity() != null
						&& castOther.getCapacity() != null && this
						.getCapacity().equals(castOther.getCapacity())))
				&& ((this.getStatus() == castOther.getStatus()) || (this
						.getStatus() != null && castOther.getStatus() != null && this
						.getStatus().equals(castOther.getStatus())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getId();
		result = 37 * result
				+ (getName() == null ? 0 : this.getName().hashCode());
		result = 37
				* result
				+ (getDescription() == null ? 0 : this.getDescription()
						.hashCode());
		result = 37 * result
				+ (getCapacity() == null ? 0 : this.getCapacity().hashCode());
		result = 37 * result
				+ (getStatus() == null ? 0 : this.getStatus().hashCode());
		return result;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

}
