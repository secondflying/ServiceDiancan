package com.chihuo.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chihuo.bussiness.Category;
import com.chihuo.dao.CategoryDao;
import com.chihuo.util.HibernateUtil﻿;

public class HibernateTest {

	private SessionFactory sf;

	@Before
	public void setUp() throws Exception {
		sf = HibernateUtil﻿.getSessionFactory();
		sf.getCurrentSession().getTransaction().begin();
	}

	@After
	public void tearDown() throws Exception {
		try {
			sf.getCurrentSession().getTransaction().commit();
		} catch (StaleObjectStateException staleEx) {
			staleEx.printStackTrace();
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (sf.getCurrentSession().getTransaction().isActive()) {
				sf.getCurrentSession().getTransaction().rollback();
			}
		}
	}

	@Test
	public void test() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			Date date = dateFormat.parse("20120223004000");
			CategoryDao dao= new CategoryDao();
			List<Category> addList = dao.queryAddedList(date);
			System.out.println(addList.size());
			
			List<Category> updateList = dao.queryUpdatedList(date);
			System.out.println(updateList.size());
			
			List<Integer> deleteList = dao.queryDeletedList(date);
			System.out.println(deleteList.size());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void test2() {
	}
}
