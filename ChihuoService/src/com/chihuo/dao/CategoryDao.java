package com.chihuo.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.chihuo.bussiness.Category;
import com.chihuo.resource.MyConstants;

public class CategoryDao extends GenericHibernateDAO﻿<Category, Integer> {
	@Override
	public Category saveOrUpdate(Category entity) {
		getSession().saveOrUpdate(entity);

		DaoUtil.saveOrUpdateLog(MyConstants.CATEGORY_TABLE, entity.getId());
		return entity;
	}
	
	@Override
	public void delete(Category entity) {
		getSession().delete(entity);
		
		DaoUtil.deleteLog(MyConstants.CATEGORY_TABLE, entity.getId());
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> queryAddedList(Date date){
		String hql = "select c from Category as c, DataLog as log where c.id = log.did and log.dataTable=:tablename and log.addTime>=:begin and log.deleteTime is null";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.CATEGORY_TABLE);
		query.setTimestamp("begin", date);
		List<Category> list = (List<Category>)query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> queryUpdatedList(Date date){
		String hql = "select c from Category as c, DataLog as log where c.id = log.did and log.dataTable=:tablename and log.addTime<:begin and log.updateTime>=:begin and log.deleteTime is null";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.CATEGORY_TABLE);
		query.setTimestamp("begin", date);
		List<Category> list = (List<Category>)query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> queryDeletedList(Date date){
		String hql = "select log.did from DataLog as log where log.dataTable=:tablename and log.addTime<:begin and log.deleteTime>=:begin";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.CATEGORY_TABLE);
		query.setTimestamp("begin", date);
		List<Integer> list = (List<Integer>)query.list();
		return list;
	}
}
