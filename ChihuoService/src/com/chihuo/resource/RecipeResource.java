package com.chihuo.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Category;
import com.chihuo.bussiness.DataLog;
import com.chihuo.bussiness.Recipe;
import com.chihuo.dao.CategoryDao;
import com.chihuo.dao.DataLogDao;
import com.chihuo.dao.RecipeDao;

public class RecipeResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;

	public RecipeResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// 获取单个菜单的描述信息
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe getRecipeInfo() {
		RecipeDao dao = new RecipeDao();
		Recipe recipe = dao.findById(id);
		if (recipe == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		// TODO 为image添加绝对路径 使用jaxb marshalling unmarshalling
		// UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		// URI userUri = ub.path("MenuImages").build();
		// String uri = uriInfo.getBaseUri().toString();

		// if(recipe.getImage() != null){
		// recipe.setImage(uriInfo.getBaseUri().toString() + "MenuImages" +
		// recipe.getImage());
		// }
		return recipe;
	}
	
	
	@GET
	@Produces("image/*")
	public Response getRecipeImage() {
		RecipeDao dao = new RecipeDao();
		Recipe recipe = dao.findById(id);
		if (recipe == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		File file = new File(MyConstants.MenuImagePath + recipe.getImage());
		String mt = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mt).build();
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON })
	public Response updateRecipeInfo(String string) {
		RecipeDao dao = new RecipeDao();
		Recipe recipe = dao.findById(id);
		if (recipe == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.has("name")) {
				recipe.setName(jsonObject.getString("name"));
			}
			if (jsonObject.has("price")) {
				recipe.setPrice(jsonObject.getInt("price"));
			}
			if (jsonObject.has("description")) {
				recipe.setDescription(jsonObject.getString("description"));
			}
			if (jsonObject.has("cid")) {
				int cid = jsonObject.getInt("cid");

				CategoryDao cdao = new CategoryDao();
				Category category = cdao.findById(cid);
				if (category == null) {
					throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
							.entity("种类ID不存在").type(MediaType.TEXT_PLAIN).build());
				}
				recipe.setCategory(category);
			}

			dao.saveOrUpdate(recipe);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (JSONException e1) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("创建菜单失败").type(MediaType.TEXT_PLAIN).build();
		}
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM })
	public Response updateRecipeImage(InputStream stream) {
		RecipeDao dao = new RecipeDao();
		Recipe c = dao.findById(id);
		if (c == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		File file = new File(MyConstants.MenuImagePath + c.getImage());
		if (file.isFile()) {
			file.delete();
		}

		try {
			BufferedImage bi = ImageIO.read(stream);
			ImageIO.write(bi, "png", file);

			DataLogDao ldao = new DataLogDao();
			DataLog log = ldao.findByTableDid(MyConstants.RECIPE_TABLE,
					c.getId());
			if (log != null) {
				log.setUpdateTime(new Date());
				ldao.saveOrUpdate(log);
			}
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("创建菜单失败").type(MediaType.TEXT_PLAIN).build();
		}
	}

	@DELETE
	public void deleteRecipe() {
		RecipeDao dao = new RecipeDao();
		Recipe c = dao.findById(id);
		if (c == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		File file = new File(MyConstants.MenuImagePath + c.getImage());
		if (file.isFile()) {
			file.delete();
		}
		dao.delete(c);
	}
}
