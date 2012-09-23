package com.chihuo.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.chihuo.bussiness.DeskStatusView;

public class DeskStatusDao extends GenericHibernateDAO﻿<DeskStatusView, Integer> {
	
	public boolean isDeskCanOrder(int did) {
		DeskStatusView view = findById(did);
		return view.getStatus() == null || view.getStatus() == 3 || view.getStatus() == 4;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeskStatusView> queryByTid(int tid){
		Criteria crit = getSession().createCriteria(DeskStatusView.class).add(Restrictions.eq("tid", tid)).addOrder(Order.asc("id"));
		return (List<DeskStatusView>)crit.list();
	}
}
