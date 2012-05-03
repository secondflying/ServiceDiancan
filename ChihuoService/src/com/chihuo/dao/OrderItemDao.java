package com.chihuo.dao;


import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.chihuo.bussiness.OrderItem;

public class OrderItemDao extends GenericHibernateDAO﻿<OrderItem, Integer> {
	public OrderItem queryByOrderAndRecipe(int oid, int rid) {
		Criteria crit = getSession().createCriteria(OrderItem.class);
		crit.createCriteria("order").add(Restrictions.eq("id", oid));
		crit.createCriteria("recipe").add(Restrictions.eq("id", rid));
		return (OrderItem) crit.uniqueResult();
	}
}
