package com.chihuo.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import com.chihuo.util.NotificationHelper;
import com.chihuo.util.NotificationHelper.NotificationType;

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

	// 加减菜
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRecipe(String jsonString) throws JSONException {
		// System.out.print(jsonString);
		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else if (order.getStatus() != null && order.getStatus() != 1) {
			// TODO 判断该台号是否可以加减菜
			return Response.status(Response.Status.CONFLICT)
					.entity("桌号为" + id + "的桌子不可加减菜").type(MediaType.TEXT_PLAIN)
					.build();
		}

		JSONObject jsonObject = new JSONObject(jsonString);
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

		// TODO 加减菜时给服务员发送通知
		NotificationHelper.sendNotifcationToUser(NotificationType.AddMenu,
				order.getId() + "", order.getWaiter().getUsername());

		// redirect
		URI uri = uriInfo.getRequestUri();
		return Response.seeOther(uri).build();

		// 还未提交
		// return Response.ok(order).build();
	}

	// 改变订单状态，如结账，撤单等
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response alterOrderStatus(String jsonString) throws JSONException {
		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		JSONObject jsonObject = new JSONObject(jsonString);
		int status = jsonObject.getInt("status");

		if (status == Integer.parseInt(NotificationType.RequestCheckOut
				.toString())) {
			// 请求结账
			order.setStatus(3);
			dao.saveOrUpdate(order);

			// TODO 给服务员发送通知
			NotificationHelper.sendNotifcationToUser(NotificationType.CheckOut,
					NotificationHelper.getDeskString(order.getId(),order.getDesk()), order.getWaiter()
							.getUsername());

			return Response.status(Response.Status.OK).entity(order)
					.type(MediaType.APPLICATION_JSON).build();
		} else if (status == Integer.parseInt(NotificationType.CheckOut
				.toString())) {
			// 结账
			order.setStatus(4);
			dao.saveOrUpdate(order);

			return Response.status(Response.Status.OK).entity(order)
					.type(MediaType.APPLICATION_JSON).build();
		} else if (status == Integer.parseInt(NotificationType.CheckIn
				.toString())) {
			// 撤单
			order.setStatus(5);
			dao.saveOrUpdate(order);

			return Response.status(Response.Status.OK).entity(order)
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("修改状态失败").type(MediaType.TEXT_PLAIN).build();
		}
	}

	// 改变菜的状态，如以上，
	@Path("{iid}")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response alterOrderItemStatus(@PathParam("iid") int iid) {
		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		OrderItemDao oDao = new OrderItemDao();
		OrderItem oi = oDao.findById(iid);
		if (oi == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("ID为" + iid + "的已点菜不存在").type(MediaType.TEXT_PLAIN)
					.build();
		}

		// 修改状态为已点
		oi.setStatus(1);

		oDao.saveOrUpdate(oi);

		order = dao.findById(id);

		return Response.status(Response.Status.OK).entity(order)
				.type(MediaType.APPLICATION_JSON).build();
	}

	// 其他服务，呼叫服务员等
	@Path("/assistent")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response AssistentHelp(String jsonString) throws JSONException {
		// System.out.print(jsonString);
		OrderDao dao = new OrderDao();
		Order order = dao.findById(id);
		if (order == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		JSONObject jsonObject = new JSONObject(jsonString);
		int type = jsonObject.getInt("type");
		if (type == Integer.parseInt(NotificationType.AddWater.toString())) {
			NotificationHelper.sendNotifcationToUser(NotificationType.AddWater,
					NotificationHelper.getDeskString(order.getId(),order.getDesk()), order.getWaiter()
							.getUsername());
			return Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).build();
		} else if (type == Integer
				.parseInt(NotificationType.AddDish.toString())) {
			NotificationHelper.sendNotifcationToUser(NotificationType.AddDish,
					NotificationHelper.getDeskString(order.getId(),order.getDesk()), order.getWaiter()
							.getUsername());
			return Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).build();
		} else if (type == Integer.parseInt(NotificationType.CallWaiter
				.toString())) {
			NotificationHelper.sendNotifcationToUser(
					NotificationType.CallWaiter, NotificationHelper.getDeskString(order.getId(),order.getDesk()),
					order.getWaiter().getUsername());
			return Response.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST)
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

}
