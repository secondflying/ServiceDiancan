package com.chihuo.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.chihuo.bussiness.Desk;
import com.chihuo.bussiness.Order;
import com.chihuo.resource.MyConstants;

public class DeskDao extends GenericHibernateDAO﻿<Desk, Integer> {
	@Override
	public Desk saveOrUpdate(Desk entity) {
		getSession().saveOrUpdate(entity);

		DaoUtil.saveOrUpdateLog(MyConstants.DESK_TABLE, entity.getId());
		return entity;
	}

	@Override
	public void delete(Desk entity) {
		getSession().delete(entity);
		DaoUtil.deleteLog(MyConstants.DESK_TABLE, entity.getId());
	}
	
	@SuppressWarnings("unchecked")
	public List<Desk> queryAddedList(Date date){
		String hql = "select c from Desk as c, DataLog as log where c.id = log.did and log.dataTable=:tablename and log.addTime>=:begin and log.deleteTime is null";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.DESK_TABLE);
		query.setTimestamp("begin", date);
		List<Desk> list = (List<Desk>)query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Desk> queryUpdatedList(Date date){
		String hql = "select c from Desk as c, DataLog as log where c.id = log.did and log.dataTable=:tablename and log.addTime<:begin and log.updateTime>=:begin and log.deleteTime is null";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.DESK_TABLE);
		query.setTimestamp("begin", date);
		List<Desk> list = (List<Desk>)query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> queryDeletedList(Date date){
		String hql = "select log.did from DataLog as log where log.dataTable=:tablename and log.deleteTime is not null and log.deleteTime>=:begin";
		Query query = getSession().createQuery(hql);
		query.setString("tablename", MyConstants.DESK_TABLE);
		query.setTimestamp("begin", date);
		List<Integer> list = (List<Integer>)query.list();
		return list;
	}
}
