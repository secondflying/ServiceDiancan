package com.chihuo.dao;

import com.chihuo.bussiness.DeskStatusView;

public class DeskStatusDao extends GenericHibernateDAO﻿<DeskStatusView, Integer> {
	
	public boolean isDeskCanOrder(int did) {
		DeskStatusView view = findById(did);
		return view.getStatus() == null || view.getStatus() != 1;
	}
}
