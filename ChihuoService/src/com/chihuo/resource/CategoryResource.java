package com.chihuo.resource;

import java.io.File;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.chihuo.bussiness.Category;
import com.chihuo.bussiness.Recipe;
import com.chihuo.dao.CategoryDao;

public class CategoryResource {
	UriInfo uriInfo;
	Request request;
	int id;

	public CategoryResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getCategory() {
		CategoryDao dao = new CategoryDao();
		Category c = dao.findById(id);
		if (c == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		Set<Recipe> recipes = c.getRecipes();
		GenericEntity<Set<Recipe>> entity = new GenericEntity<Set<Recipe>>(
				recipes) {
		};
		Response.ResponseBuilder builder = Response.ok(entity);
		
		//添加缓存设置
//		CacheControl cc = new CacheControl();
//		cc.setMaxAge(30000);
//		cc.setPrivate(false);
//		builder.cacheControl(cc);
		return builder.build();
	}

	@GET
	@Produces("image/*")
	public Response getCategoryImage() {
		CategoryDao dao = new CategoryDao();
		Category recipe = dao.findById(id);
		if (recipe == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		File file = new File(MyConstants.MenuImagePath + recipe.getImage());
		String mt = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mt).build();
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void updateCategory(JAXBElement<Category> category) {
		CategoryDao dao = new CategoryDao();
		Category c = dao.findById(id);
		if (c == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		c.setName(category.getValue().getName());
		c.setDescription(category.getValue().getDescription());
		dao.saveOrUpdate(c);
	}

	@DELETE
	public void deleteCategory() {
		CategoryDao dao = new CategoryDao();
		Category c = dao.findById(id);
		if (c == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		dao.delete(c);
	}

}
