package com.chihuo.bussiness;

// Generated 2012-2-28 10:39:34 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.chihuo.util.JaxbDateSerializer;

/**
 * Order generated by hbm2java
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements java.io.Serializable {
	@XmlElement
	private Integer id;
	@XmlElement
	private Desk desk;
	@XmlElement
	private Waiter waiter;
	
	@XmlElement
	private Integer number;
	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	private Date starttime;
	private Date ordertime;
	private Date enttime;
	@XmlElement
	private Integer status;
	@XmlElement
	private String code;
	@XmlElement
	private Set<OrderItem> orderItems = new HashSet<OrderItem>(0);

	public Order() {
	}

	public Order(Desk desk) {
		this.desk = desk;
	}

	public Order(Desk desk, Integer number, Date starttime, Date ordertime,
			Date enttime, Integer status, Set<OrderItem> orderItems) {
		this.desk = desk;
		this.number = number;
		this.starttime = starttime;
		this.ordertime = ordertime;
		this.enttime = enttime;
		this.status = status;
		this.orderItems = orderItems;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Desk getDesk() {
		return this.desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Date getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getOrdertime() {
		return this.ordertime;
	}

	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	public Date getEnttime() {
		return this.enttime;
	}

	public void setEnttime(Date enttime) {
		this.enttime = enttime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Set<OrderItem> getOrderItems() {
		return this.orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Waiter getWaiter() {
		return waiter;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
}
