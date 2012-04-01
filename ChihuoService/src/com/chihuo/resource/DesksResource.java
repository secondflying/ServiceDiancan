package com.chihuo.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.chihuo.bussiness.Desk;
import com.chihuo.dao.DeskDao;

@Path("/desks")
public class DesksResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Desk> getCategories() {
		DeskDao dao = new DeskDao();
		List<Desk> list = dao.findAll();
		return list;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCategory(JAXBElement<Desk> c) {
		Desk newCatagory = c.getValue();
		DeskDao dao = new DeskDao();
		Desk createdCategory = dao.saveOrUpdate(newCatagory);
		return Response.created(URI.create(createdCategory.getId() + "")).build();
	}
}
