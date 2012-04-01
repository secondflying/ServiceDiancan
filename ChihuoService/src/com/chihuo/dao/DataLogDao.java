package com.chihuo.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.chihuo.bussiness.DataLog;

public class DataLogDao extends GenericHibernateDAO﻿<DataLog, Integer> {
	public DataLog findByTableDid(String table,int did){
		Criteria crit = getSession().createCriteria(DataLog.class).add(Restrictions.eq("dataTable", table)).add(Restrictions.eq("did", did));
		return (DataLog)crit.uniqueResult();
	}
}
