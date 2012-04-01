package com.chihuo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpClientOrderTest {

	DefaultHttpClient client;
	String rootUrl = "http://localhost.:8080/ChihuoService/rest";

	@Before
	public void setUp() throws Exception {
		client = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("localhost", 8888);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	@After
	public void tearDown() throws Exception {
		client.getConnectionManager().shutdown();
	}
	
	 @Test
	public void 获取单个订单() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("获取单个订单:");
		HttpGet get = new HttpGet(rootUrl + "/orders/3");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));
	}
	 
//	 com.chihuo.diancan
	

	@Test
	public void 提交订单() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("提交订单:");
		HttpPost post = new HttpPost(rootUrl + "/orders");
		
		JSONArray list = new JSONArray();
		for (int i = 0; i < 4; i++) {
			JSONObject obj = new JSONObject();
			obj.put("rid", 153 + i);
			obj.put("count", 1);
			list.put(obj);
		}

		JSONObject object = new JSONObject();
		object.put("tid", 1);
		object.put("number", 4);
		object.put("recipes", list);
		System.out.println(object.toString());

		StringEntity entity = new StringEntity(object.toString(), "UTF-8");
		entity.setContentType("application/json;charset=UTF-8");
		entity.setContentEncoding("UTF-8");

		post.setEntity(entity);
		post.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpClientParams.setRedirecting(post.getParams(), false);

		HttpResponse response = client.execute(post);
		Assert.assertEquals(201, response.getStatusLine().getStatusCode());

		String location = response.getLastHeader("Location").getValue();
		System.out.println("创建成功：" + location);

		// 多次执行报下面的错误，加上下面这一句解决
		EntityUtils.consume(response.getEntity());
	}

	private String showContent(InputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			sb.append(line);
			line = reader.readLine();
		}
		reader.close();
		return sb.toString();
	}
}
