package com.chihuo.util;

import javax.servlet.ServletException;

import org.androidpn.server.xmpp.push.NotificationManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

public class MyServletContainer extends com.sun.jersey.spi.container.servlet.ServletContainer {
	private static final long serialVersionUID = 4988964268059005102L;
	private static Log log = LogFactory
			.getLog(MyServletContainer.class);
	SessionFactory sf;
	
	@Override
	public void init() throws ServletException {
		sf = HibernateUtil﻿.getSessionFactory();
		
		//初始化XMPP 服务器端
		NotificationManager notificationManager = new NotificationManager();

		super.init();
	}

	@Override
	public void service(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

		try {
			sf.getCurrentSession().beginTransaction();
			super.service(request, response);
			sf.getCurrentSession().getTransaction().commit();
		} catch (Throwable ex) {
			//TODO 这里应该只捕获hibernate操作的异常
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
