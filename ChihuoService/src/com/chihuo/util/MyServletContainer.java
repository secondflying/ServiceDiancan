package com.chihuo.util;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;

public class MyServletContainer extends com.sun.jersey.spi.container.servlet.ServletContainer {
	private static Log log = LogFactory
			.getLog(MyServletContainer.class);
	SessionFactory sf;
	
	@Override
	public void init() throws ServletException {
		sf = HibernateUtil﻿.getSessionFactory();
		super.init();
	}

	@Override
	public void service(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

		try {
			sf.getCurrentSession().beginTransaction();
			super.service(request, response);
			sf.getCurrentSession().getTransaction().commit();
		} catch (Throwable ex) {
			try {
				if (sf.getCurrentSession().getTransaction().isActive()) {
					log.debug("Trying to rollback database transaction after exception");
					sf.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				log.error("Could not rollback transaction after exception!",
						rbEx);
			}
		} 
	}

}
