package com.chihuo.resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.chihuo.bussiness.AllDomain;
import com.chihuo.bussiness.Category;
import com.chihuo.bussiness.Desk;
import com.chihuo.bussiness.DeskStatusView;
import com.chihuo.bussiness.Order;
import com.chihuo.bussiness.OrderItem;
import com.chihuo.bussiness.Recipe;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	private final JAXBContext context;

	private final Set<Class> types;

	private Class[] ctypes = { Category.class, Recipe.class, Desk.class, DeskStatusView.class, Order.class, OrderItem.class, AllDomain.class };

	public JAXBContextResolver() throws Exception {
		this.types = new HashSet(Arrays.asList(ctypes));
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
				ctypes);
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {
		return (types.contains(objectType)) ? context : null;
	}
}
