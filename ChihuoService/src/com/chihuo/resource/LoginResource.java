package com.chihuo.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/login")
public class LoginResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	HttpServletRequest httpRequest;
	@Context
	HttpServletResponse httpResponse;

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public String createCategory(@FormParam("username") String username, @FormParam("password") String password) {
		System.out.println(username);
		System.out.println(password);
		
		Cookie cookie = new Cookie("username",username);
//		cookie.setHttpOnly(true);
		cookie.setMaxAge(2000);
		httpResponse.addCookie(cookie);
		return "<root>success</root>";
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String testCookie() {
		//打印cookie
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			System.out.println("Request Cookie: ");
			for (Cookie cookie : cookies) {
	            System.out.println("name: " + cookie.getName() + "  value:" + cookie.getValue() + "  age:" + cookie.getMaxAge() + "  secure:" + cookie.getSecure());
			}
		}
		
		Cookie cookie = new Cookie("cookiedemo","cookievalue");
//		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(2000);
		httpResponse.addCookie(cookie);
		return "<root>success</root>";
	}
}
