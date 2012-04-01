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
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpClientAllDomainTest {

	DefaultHttpClient client;
	String rootUrl = "http://localhost.:8080/ChihuoService/rest";

	@Before
	public void setUp() throws Exception {
		client = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("localhost", 8888);
//		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	@After
	public void tearDown() throws Exception {
		client.getConnectionManager().shutdown();
	}

	@Test
	public void 获取所有离线数据() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("获取所有离线数据:");
		HttpGet get = new HttpGet(rootUrl + "/all");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));
	}

	@Test
	public void 获取增量数据() throws ClientProtocolException, IOException {
		System.out.println("获取增量数据:");
		HttpGet get = new HttpGet(rootUrl + "/all/20120223004430");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));
	}

	private String showContent(InputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String line = reader.readLine();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}
		reader.close();
		return sb.toString();
	}
}
