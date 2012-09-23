package com.chihuo.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.chihuo.bussiness.Waiter;

public class WaiterDao extends GenericHibernateDAO﻿<Waiter, Integer> {
	public Waiter getUserByUsername(String username){
		Criteria crit = getSession().createCriteria(Waiter.class).add(Restrictions.eq("username", username));
		return (Waiter)crit.uniqueResult();
	}
}
