package com.chihuo.resource;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Order;
import com.chihuo.bussiness.OrderItem;
import com.chihuo.bussiness.Recipe;
import com.chihuo.dao.OrderDao;
import com.chihuo.dao.OrderItemDao;
import com.chihuo.dao.RecipeDao;

public class OrderResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;

	public OrderResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Order getOrder() {
		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return order;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRecipe(String jsonString) throws JSONException {
		System.out.println(jsonString);

		JSONObject jsonObject = new JSONObject(jsonString);

		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		// TODO 判断该台号是否可以加减菜
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		else if (order.getStatus() != null && order.getStatus() != 1) {
			return Response.status(Response.Status.CONFLICT)
					.entity("桌号为" + id + "的桌子不可加减菜").type(MediaType.TEXT_PLAIN)
					.build();
		}

		int rid = jsonObject.getInt("rid");
		RecipeDao rDao = new RecipeDao();
		Recipe recipe = rDao.findById(rid);
		if (recipe == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("编号为" + id + "的菜不存在").type(MediaType.TEXT_PLAIN)
					.build();
		}

		int count = jsonObject.getInt("count");

		OrderItemDao idao = new OrderItemDao();
		OrderItem item = idao.queryByOrderAndRecipe(id, rid);

		int totalCount = 0;
		if (item != null && item.getCount() != null) {
			totalCount = item.getCount() + count;
		} else {
			totalCount += count;
		}
		totalCount = totalCount < 0 ? 0 : totalCount;

		if (totalCount == 0) {
			if (item != null) {
				idao.delete(item);
			}
		} else {
			if (item == null) {
				item = new OrderItem();
			}
			item.setOrder(order);
			item.setRecipe(recipe);
			item.setCount(totalCount);
			idao.saveOrUpdate(item);
		}

		// redirect
		URI uri = uriInfo.getRequestUri();
		return Response.seeOther(uri).build();

		// 还未提交
		// return Response.ok(order).build();
	}

	// @DELETE
	// @Consumes({MediaType.APPLICATION_JSON})
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response deleteRecipe() {
	// return null;
	// }

}
