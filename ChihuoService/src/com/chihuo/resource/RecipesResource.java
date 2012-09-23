package com.chihuo.resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

import com.chihuo.bussiness.Category;
import com.chihuo.bussiness.Recipe;
import com.chihuo.dao.CategoryDao;
import com.chihuo.dao.RecipeDao;
import com.sun.jersey.multipart.FormDataParam;

@Path("/recipes")
public class RecipesResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
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
	public Response createRecipe2(@FormDataParam("name") String name,
			@FormDataParam("price") int price,
			@FormDataParam("description") String description,
			@DefaultValue("-1") @FormDataParam("cid") int cid,
			@FormDataParam("image") InputStream upImg) {

		// return Response.status(Response.Status.BAD_REQUEST)
		// .entity("创建菜单失败").type(MediaType.TEXT_PLAIN).build();

		// return Response.created(URI.create(String.valueOf(11))).build();

		Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setPrice(price);
		recipe.setDescription(description);

		if (cid != -1) {
			CategoryDao cdao = new CategoryDao();
			Category category = cdao.findById(cid);
			if (category == null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("种类ID不存在").type(MediaType.TEXT_PLAIN).build();
			}
			recipe.setCategory(category);
		}

		if (upImg != null) {
			try {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[16384];
				while ((nRead = upImg.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();
				byte[] bs = buffer.toByteArray();

				if (bs.length > 0) {
					String id = UUID.randomUUID().toString();
					String image = id + ".png";
					recipe.setImage(image);

					BufferedImage bi = ImageIO
							.read(new ByteArrayInputStream(bs));

					File file = new File(MyConstants.MenuImagePath + image);
					if (file.isDirectory()) {
						ImageIO.write(bi, "png", file);
					} else {
						file.mkdirs();
						ImageIO.write(bi, "png", file);
					}
				}

			} catch (IOException e) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("创建菜单失败").type(MediaType.TEXT_PLAIN).build();
			}
		}

		RecipeDao dao = new RecipeDao();
		dao.saveOrUpdate(recipe);

		return Response.created(URI.create(String.valueOf(recipe.getId())))
				.build();
	}
}
