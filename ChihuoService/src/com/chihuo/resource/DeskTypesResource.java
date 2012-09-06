package com.chihuo.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.chihuo.bussiness.DeskStatusView;
import com.chihuo.bussiness.DeskType;
import com.chihuo.dao.DeskStatusDao;
import com.chihuo.dao.DeskTypeDao;

@Path("/desktypes")
public class DeskTypesResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces("application/json; charset=UTF-8")
	public List<DeskType> getDeskTypes() {
		DeskTypeDao dao = new DeskTypeDao();
		List<DeskType> list = dao.findAll();
		return list;
	}

	@Path("{id}")
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getDesks(@PathParam("id") int id) {
		DeskTypeDao dao = new DeskTypeDao();
		DeskType c = dao.findById(id);
		if (c == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		DeskStatusDao ddao = new DeskStatusDao();
		List<DeskStatusView> list = ddao.queryByTid(id);
		GenericEntity<List<DeskStatusView>> entity = new GenericEntity<List<DeskStatusView>>(list) {
		};
		Response.ResponseBuilder builder = Response.ok(entity);
		return builder.build();
	}
	
}
