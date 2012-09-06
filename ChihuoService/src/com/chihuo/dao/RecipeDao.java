package com.chihuo.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.chihuo.bussiness.Recipe;
import com.chihuo.resource.MyConstants;

public class RecipeDao extends GenericHibernateDAO﻿<Recipe, Integer> {
	@Override
	public Recipe saveOrUpdate(Recipe entity) {
		getSession().saveOrUpdate(entity);

		DaoUtil.saveOrUpdateLog(MyConstants.RECIPE_TABLE, entity.getId());
		return entity;
	}
	
	@Override
	public void delete(Recipe entity) {
		entity.setIsdelete(1);
		getSession().saveOrUpdate(entity);
		DaoUtil.deleteLog(MyConstants.RECIPE_TABLE, entity.getId());
	}
	
	@SuppressWarnings("unchecked")
	public List<Recipe> findByCategory(int cid){
		Criteria crit = getSession().createCriteria(Recipe.class).add(Restrictions.not(Restrictions.eq("delete", 1))).createCriteria("category").add(Restrictions.eq("id", cid));
		
		return (List<Recipe>)crit.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Recipe> queryAddedList(Date date){
		String hql = "select c from Recipe as c, DataLog as log where c.id = log.did and log.dataTable=:tablename and log.addTime>=:begin and log.deleteTime is null";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.RECIPE_TABLE);
		query.setTimestamp("begin", date);
		return (List<Recipe>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Recipe> queryUpdatedList(Date date){
		//TODO 菜单的更新状态有两种，文字和图片
		String hql = "select c from Recipe as c, DataLog as log where c.id = log.did and log.dataTable=:tablename and log.addTime<:begin and log.updateTime>=:begin and log.deleteTime is null";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.RECIPE_TABLE);
		query.setTimestamp("begin", date);
		return (List<Recipe>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> queryDeletedList(Date date){
		String hql = "select log.did from DataLog as log where log.dataTable=:tablename and log.deleteTime is not null and log.deleteTime>=:begin";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.RECIPE_TABLE);
		query.setTimestamp("begin", date);
		return (List<Integer>)query.list();
	}
}
