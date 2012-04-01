package com.chihuo.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Category;
import com.chihuo.bussiness.Recipe;
import com.chihuo.dao.CategoryDao;
import com.chihuo.dao.RecipeDao;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

@Path("/recipes")
public class RecipesResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_XML })
	public List<Recipe> getAllRecipes() {
		RecipeDao dao = new RecipeDao();
		return dao.findAll();
	}

	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		RecipeDao dao = new RecipeDao();
		return String.valueOf(dao.findAll().size());
	}

	@Path("{id}")
	public RecipeResource getRecipe(@PathParam("id") int id) {
		return new RecipeResource(uriInfo, request, id);
	}

	@POST
	@Consumes("multipart/form-data")
	public Response createRecipe2(MultiPart multipart) {
		Recipe recipe = new Recipe();

		try {
			String string = multipart.getBodyParts().get(0)
					.getEntityAs(String.class);
			JSONObject jsonObject = new JSONObject(string);
			recipe.setName(jsonObject.getString("name"));
			recipe.setPrice(jsonObject.getInt("price"));
			recipe.setDescription(jsonObject.has("description") ? jsonObject
					.getString("description") : null);

			if (jsonObject.has("cid")) {
				int cid = jsonObject.getInt("cid");

				CategoryDao cdao = new CategoryDao();
				Category category = cdao.findById(cid);
				if (category == null) {
					return Response.status(Response.Status.BAD_REQUEST)
							.entity("种类ID不存在").type(MediaType.TEXT_PLAIN)
							.build();
				}
				recipe.setCategory(category);
			}
		} catch (JSONException e1) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("创建菜单失败").type(MediaType.TEXT_PLAIN).build();
		}

		BodyPartEntity bpe = (BodyPartEntity) multipart.getBodyParts().get(1)
				.getEntity();
		String id = UUID.randomUUID().toString();
		String image = id + ".png";
		recipe.setImage(image);

		try {
			InputStream source = bpe.getInputStream();
			BufferedImage bi = ImageIO.read(source);

			File file = new File(MyConstants.MenuImagePath + image);
			if (file.isDirectory()) {
				ImageIO.write(bi, "png", file);
			} else {
				file.mkdirs();
				ImageIO.write(bi, "png", file);
			}
			
			RecipeDao dao = new RecipeDao();
			dao.saveOrUpdate(recipe);

			return Response.created(URI.create(String.valueOf(recipe.getId())))
					.build();

		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("创建菜单失败").type(MediaType.TEXT_PLAIN).build();
		}

	}
}
