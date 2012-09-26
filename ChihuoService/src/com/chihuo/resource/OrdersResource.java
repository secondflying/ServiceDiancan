package com.chihuo.resource;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Desk;
import com.chihuo.bussiness.Order;
import com.chihuo.bussiness.Waiter;
import com.chihuo.dao.DeskDao;
import com.chihuo.dao.DeskStatusDao;
import com.chihuo.dao.OrderDao;
import com.chihuo.dao.WaiterDao;

@Path("/orders")
public class OrdersResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Path("{id}")
	public OrderResource getOrder(@PathParam("id") int id) {
		return new OrderResource(uriInfo, request, id);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Order> getOrders(@QueryParam("status") int status) {
		//获取订单信息，默认获取未结订单
		
		OrderDao odao = new OrderDao();
		return odao.findByStatus(status);
	}

	// 服务员开台
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOrder2(String jsonString) throws Exception {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			//判断桌子是否能开台
			int tid = jsonObject.getInt("tid");
			DeskDao cdao = new DeskDao();
			DeskStatusDao sdao = new DeskStatusDao();
			Desk d = cdao.findById(tid);
			if (d == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("桌号为" + tid + "的桌子不存在")
						.type(MediaType.TEXT_PLAIN).build();
			} else if (!sdao.isDeskCanOrder(d.getId())) {
				return Response.status(Response.Status.CONFLICT)
						.entity("桌号为" + tid + "的桌子不可开台")
						.type(MediaType.TEXT_PLAIN).build();
			}
			
			//判断服务员的用户名是否正确
			String username = jsonObject.getString("username");
			WaiterDao wdao = new WaiterDao();
			Waiter waiter = wdao.getUserByUsername(username);
			if (waiter == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("名称为'" + username + "'的服务员不存在")
						.type(MediaType.TEXT_PLAIN).build();
			}

			// TODO order状态： 1为新开台 3为请求结账 4为已结账 5为撤单
			Order order = new Order();
			order.setDesk(d);
			order.setWaiter(waiter);
			order.setNumber(jsonObject.getInt("number"));
			order.setStarttime(new Date());
			order.setStatus(1);

			// TODO 在生成的code里面包含桌号，避免同时有相同code的order
			order.setCode(Math.round(Math.random() * 9000 + 1000) + "");

			OrderDao odao = new OrderDao();
			odao.saveOrUpdate(order);

			return Response.created(URI.create(String.valueOf(order.getId()))).entity(order)
					.type(MediaType.APPLICATION_JSON).build();
		} catch (JSONException e) {
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST).entity("创建订单失败")
					.type(MediaType.TEXT_PLAIN).build());
		}
	}

	// 根据编码获取订单
	@GET
	@Path("desk/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderByCode(@PathParam("code") String code) {
		OrderDao odao = new OrderDao();
		Order order = odao.findByCode(code);
		if (order == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("编码错误")
					.type(MediaType.TEXT_PLAIN).build();
		}

		return Response.status(Response.Status.OK).entity(order)
				.type(MediaType.APPLICATION_JSON).build();
	}

}
