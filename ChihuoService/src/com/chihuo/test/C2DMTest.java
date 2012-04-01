package com.chihuo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.WebResource;

public class C2DMTest {

	WebResource service;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testGetToken() {
		try {
			String token = getToken(USER,PASSWORD);
			System.out.println(token);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSendMessage(){
		String AUTHENTICATION_TOKEN = "DQAAAK4AAAB5TgrRHkMcGUWQSb_eUOQKumpznzImbnO8emaWGOtkkufsgP9cfW13qNsUkHul44gggIyoRz67ezKxsc3W_YTS_z0VsiF0VNUv61IOVzTbucthw5ZtyhMDpDHDbRmgJcFSkfTocl0Onz7bAE3PEb7-FQ4zdOIfIp7DT4nVGyMY7-iQHtUnV8Lx3x8IkjdS1yNrZczVqmO1xRS8QUfeB4C1SH5hsAYvfaAlTMbFjILDkg";
		String REGISTRATION_ID = "APA91bGpz8rqy4FzfoZKw-KG7GUs_V1nMzfKRbvdqErq7IpoxGg4QrlgACiGYNgl3gDFC5gqAZLLr1I8E3oIQryLPrCpeJymBX3DvY9fQNMwSIY4YuNvu02Z6HFbkFlBL_BHP8QgTaW8edI2DqEWkGKWxWA0_jjSbA";
		try {
			int responseCode = MessageUtil.sendMessage(
					AUTHENTICATION_TOKEN,
			REGISTRATION_ID, "胖子帮你点了一个菜");
			System.out.println(responseCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	
	
	
	public static final String USER = "chihuoapp@gmail.com";
	public static final String PASSWORD = "]'/.;[pl,mko";
	
	
	public static String getToken(String email, String password)
			throws IOException {
		// Create the post data
		// Requires a field with the email and the password
		StringBuilder builder = new StringBuilder();
		builder.append("Email=").append(email);
		builder.append("&Passwd=").append(password);
		builder.append("&accountType=GOOGLE");
		builder.append("&source=MyLittleExample");
		builder.append("&service=ac2dm");

		byte[] data = builder.toString().getBytes();
		
		URL url = new URL("https://www.google.com/accounts/ClientLogin");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setUseCaches(false);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.setRequestProperty("Content-Length", Integer.toString(data.length));

		// Issue the HTTP POST request
		OutputStream output = con.getOutputStream();
		output.write(data);
		output.close();

		// Read the response
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String line = null;
		String auth_key = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("Auth=")) {
				auth_key = line.substring(5);
			}
		}

		// Finally get the authentication token
		// To something useful with it
		return auth_key;
	}

}
