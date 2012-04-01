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
import javax.xml.bind.JAXBElement;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Category;
import com.chihuo.dao.CategoryDao;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

@Path("/categories")
public class CategoriesResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces("application/json; charset=UTF-8")
	public List<Category> getCategories() {
		CategoryDao dao = new CategoryDao();
		List<Category> list = dao.findAll();
		return list;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCategory(JAXBElement<Category> c) {
		Category newCatagory = c.getValue();
		CategoryDao dao = new CategoryDao();
		Category createdCategory = dao.saveOrUpdate(newCatagory);
		return Response.created(URI.create(createdCategory.getId() + "")).build();
	}
	
	@POST
	@Consumes("multipart/form-data")
	public Response createCategory2(MultiPart multipart) {
		Category categor = new Category();
		
		try {
			String string = multipart.getBodyParts().get(0)
					.getEntityAs(String.class);
			JSONObject jsonObject = new JSONObject(string);
			categor.setName(jsonObject.getString("name"));
			categor.setDescription(jsonObject.has("description") ? jsonObject
					.getString("description") : null);
		} catch (JSONException e1) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("创建种类失败").type(MediaType.TEXT_PLAIN).build();
		}

		BodyPartEntity bpe = (BodyPartEntity) multipart.getBodyParts().get(1)
				.getEntity();
		String id = UUID.randomUUID().toString();
		String image = id + ".png";
		categor.setImage(image);

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
			
			CategoryDao dao = new CategoryDao();
			dao.saveOrUpdate(categor);

			return Response.created(URI.create(String.valueOf(categor.getId())))
					.build();

		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("创建种类失败").type(MediaType.TEXT_PLAIN).build();
		}
	}
	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		CategoryDao dao = new CategoryDao();
		return String.valueOf(dao.findAll().size());
	}
	
	
	@Path("{id}")
	public CategoryResource getCategoryResource(@PathParam("id") int id) {
		return new CategoryResource(uriInfo, request, id);
	}
}
