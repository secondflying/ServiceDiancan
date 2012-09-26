package com.chihuo.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.chihuo.bussiness.Order;

public class OrderDao extends GenericHibernateDAO﻿<Order, Integer> {
	public List<Order> findByStatus(int status) {
		Criteria crit = getSession().createCriteria(Order.class).add(
				Restrictions.eq("status", status));

		return (List<Order>) crit.list();
	}

	// 获取新开台的Order
	public Order findByCode(String code) {
		Criteria crit = getSession().createCriteria(Order.class)
				.add(Restrictions.eq("code", code))
				.add(Restrictions.eq("status", 1));

		return (Order) crit.uniqueResult();
	}

	public Order findByDesk(int tid, String code) {
		Criteria crit = getSession().createCriteria(Order.class)
				.add(Restrictions.eq("code", code)).createCriteria("desk")
				.add(Restrictions.eq("id", tid));
		return (Order) crit.uniqueResult();
	}
}
