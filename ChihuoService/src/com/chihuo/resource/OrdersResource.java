package com.chihuo.resource;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Desk;
import com.chihuo.bussiness.Order;
import com.chihuo.bussiness.OrderItem;
import com.chihuo.dao.DeskDao;
import com.chihuo.dao.OrderDao;
import com.chihuo.dao.OrderItemDao;
import com.chihuo.dao.RecipeDao;

@Path("/orders")
public class OrdersResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Order getCategoryResource(@PathParam("id") int id) {
		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return order;
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createOrder(String jsonString) throws Exception {
		try {
			System.out.println(jsonString);
			Order order = new Order();
			
			JSONObject jsonObject = new JSONObject(jsonString);
			
			int tid = jsonObject.getInt("tid");
			DeskDao cdao = new DeskDao();
			Desk d = cdao.findById(tid);
			if (d == null) {
				throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
						.entity("桌子ID不存在").type(MediaType.TEXT_PLAIN)
						.build());
			}
			order.setDesk(d);
			order.setNumber(jsonObject.getInt("number"));
			order.setStarttime(new Date());
			
			JSONArray list = jsonObject.getJSONArray("recipes");
			if(list.length() == 0){
				throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
						.entity("至少需要一个菜").type(MediaType.TEXT_PLAIN).build());
			}
			
			OrderDao odao = new OrderDao();
			odao.saveOrUpdate(order);
			
			OrderItemDao iDao = new OrderItemDao();
			RecipeDao rdao = new RecipeDao();
			for (int i = 0; i < list.length(); i++) {
				OrderItem oi = new OrderItem();
				oi.setOrder(order);
				oi.setRecipe(rdao.findById(list.getJSONObject(i).getInt("rid")));
				oi.setCount(list.getJSONObject(i).getInt("count"));
				iDao.saveOrUpdate(oi);
			}
			
			return Response.created(URI.create(String.valueOf(order.getId())))
					.build();
		} catch (JSONException e) {
			throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
					.entity("创建订单失败").type(MediaType.TEXT_PLAIN).build());
		}
		
	}
}