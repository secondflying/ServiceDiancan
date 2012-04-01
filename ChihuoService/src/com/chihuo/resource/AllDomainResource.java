package com.chihuo.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpRequest;

import com.chihuo.bussiness.AllDomain;
import com.chihuo.dao.CategoryDao;
import com.chihuo.dao.DeskDao;
import com.chihuo.dao.RecipeDao;

@Path("/all")
public class AllDomainResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
//	@Context
//	HttpServletRequest httpRequest;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public AllDomain getAllOfflineData() {
		CategoryDao cDao = new CategoryDao();
		RecipeDao rDao = new RecipeDao();
		DeskDao dDao = new DeskDao();
		
//		System.out.println(httpRequest.getSession(true).getId());

		AllDomain allDomain = new AllDomain(cDao.findAll(), null, null,
				rDao.findAll(), null, null, dDao.findAll(), null, null,
				new Date());
		return allDomain;
	}

	@Path("{date}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getIncrementOfflineData(@PathParam("date") String dateStr) {

		// 增量更新
		CategoryDao cDao = new CategoryDao();
		RecipeDao rDao = new RecipeDao();
		DeskDao dDao = new DeskDao();
		try {
			Date date = MyConstants.simpleDateFormat.parse(dateStr);

			AllDomain allDomain = new AllDomain(cDao.queryAddedList(date),
					cDao.queryUpdatedList(date), cDao.queryDeletedList(date),
					rDao.queryAddedList(date), rDao.queryUpdatedList(date),
					rDao.queryDeletedList(date), dDao.queryAddedList(date),
					dDao.queryUpdatedList(date), dDao.queryDeletedList(date),new Date());

			return Response.status(Response.Status.OK).entity(allDomain).build();
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("日期参数错误").type(MediaType.TEXT_PLAIN).build();
		}
	}
}
