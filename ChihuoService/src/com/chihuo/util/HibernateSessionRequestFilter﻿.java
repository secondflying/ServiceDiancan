package com.chihuo.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;		
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;

public class HibernateSessionRequestFilter﻿ implements Filter {
	private static Log log = LogFactory
			.getLog(HibernateSessionRequestFilter﻿.class);

	private SessionFactory sf;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			//TODO 当抛出WebApplication时，直接被Jax-RS库处理，到不了这里，Hibernate的事务处理有问题
			log.debug("Starting a database transaction");
			
			sf.getCurrentSession().beginTransaction();
			
			// Call the next filter (continue request processing)
			chain.doFilter(request, response);

			// Commit and cleanup
			log.debug("Committing the database transaction");
			sf.getCurrentSession().getTransaction().commit();

		} catch (StaleObjectStateException staleEx) {
			throw staleEx;
		} catch (Throwable ex) {
			// Rollback only
			try {
				if (sf.getCurrentSession().getTransaction().isActive()) {
					log.debug("Trying to rollback database transaction after exception");
					sf.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				log.error("Could not rollback transaction after exception!",
						rbEx);
			}

			// Let others handle it... maybe another interceptor for exceptions?
			throw new ServletException(ex);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.debug("Initializing filter...");
		log.debug("Obtaining SessionFactory from static HibernateUtil singleton");
		sf = HibernateUtil﻿.getSessionFactory();
	}

	@Override
	public void destroy() {

	}

}
