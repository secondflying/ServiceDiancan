package com.chihuo.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpClientRecipeTest {

	DefaultHttpClient client;
	String rootUrl = "http://localhost.:8080/ChihuoService/rest";

	@Before
	public void setUp() throws Exception {
//		Properties prop = System.getProperties();
//		// 设置http访问要使用的代理服务器的地址
//		prop.setProperty("http.proxyHost", "localhost");
//		// 设置http访问要使用的代理服务器的端口
//		prop.setProperty("http.proxyPort", "8888");
//
		client = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("localhost", 8888);
//		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	@After
	public void tearDown() throws Exception {
		client.getConnectionManager().shutdown();
	}

	@Test
	public void 新增菜单() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("新增菜单:");
		HttpPost post = new HttpPost(rootUrl + "/recipes");

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("凉菜", 45);
		map.put("汤煲", 46);
		map.put("热菜", 47);
		map.put("糕点小吃", 48);
		map.put("茶酒饮品", 49);
		String dic = "/develop/project/chihuo/svn/trunk/测试数据/menu/";
		File dictory = new File(dic);
		if (dictory.isDirectory()) {
			File[] files = dictory.listFiles();
			for (File file : files) {
				if (file.isDirectory() && !file.isHidden()) {
					
					for (File imgFile : file.listFiles()) {

						if (imgFile.isFile() && !imgFile.isHidden()) {
							String filename = imgFile.getName();
							String name = filename.substring(0,
									filename.lastIndexOf('.'));

							JSONObject object = new JSONObject();
							object.put("name", name);
							object.put("price", 60);
							object.put("cid", map.get(file.getName()));

							StringEntity entity = new StringEntity(
									object.toString(), "UTF-8");
							entity.setContentType("application/json");
							entity.setContentEncoding("UTF-8");

							StringBody recipeBody = new StringBody(
									object.toString(), "application/json",
									Charset.forName("UTF-8"));
							FileBody imageBody = new FileBody(imgFile,
									"application/octet-stream");

							MultipartEntity reqEntity = new MultipartEntity();
							reqEntity.addPart("recipe", recipeBody);
							reqEntity.addPart("recipeImage", imageBody);

							post.setEntity(reqEntity);

							HttpClientParams.setRedirecting(post.getParams(),
									false);
							HttpResponse response = client.execute(post);
							Assert.assertEquals(201, response.getStatusLine()
									.getStatusCode());

							String location = response
									.getLastHeader("Location").getValue();
							System.out.println("创建成功：" + location);

							EntityUtils.consume(response.getEntity());
							// break;
						}
					}
				}
			}
		}

		// JSONObject object = new JSONObject();
		// object.put("name", "双笋虾仁");
		// object.put("price", 200);
		//
		// StringEntity entity = new StringEntity(object.toString(), "UTF-8");
		// entity.setContentType("application/json");
		// entity.setContentEncoding("UTF-8");
		//
		// StringBody recipeBody = new StringBody(object.toString(),
		// "application/json", Charset.forName("UTF-8"));
		// FileBody imageBody = new FileBody(new File(
		// "/Users/zhaofei/Downloads/test/1.jpg"),
		// "application/octet-stream");
		//
		// MultipartEntity reqEntity = new MultipartEntity();
		// reqEntity.addPart("recipe", recipeBody);
		// reqEntity.addPart("recipeImage", imageBody);
		//
		// // String string = reqEntity.getContentType().getValue();
		// // post.setHeader("Content-Type", "multipart/mixed");
		// post.setEntity(reqEntity);
		//
		// HttpClientParams.setRedirecting(post.getParams(), false);
		//
		// HttpResponse response = client.execute(post);
		// Assert.assertEquals(201, response.getStatusLine().getStatusCode());
		//
		// String location = response.getLastHeader("Location").getValue();
		// System.out.println("创建成功：" + location);
	}

	// @Test
	public void 获取所有菜单() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("获取所有菜单:");
		HttpGet get = new HttpGet(rootUrl + "/recipes");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		String str = showContent(entity.getContent());
		JSONArray list = new JSONArray(str);
		for (int i = 0; i < list.length(); i++) {
			JSONObject obj = list.getJSONObject(i);
			System.out.println(obj.getString("name"));
		}
	}

//	 @Test
	public void 获取单个菜单() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("获取单个菜单:");
		HttpGet get = new HttpGet(rootUrl + "/recipes/153");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));
	}

	// @Test
	public void 获取菜单图片() throws ClientProtocolException, IOException {
		System.out.println("获取菜单图片:");
		HttpGet get = new HttpGet(rootUrl + "/recipes/52");
		get.addHeader("accept", "image/png");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		BufferedImage bi = ImageIO.read(entity.getContent());
		File file = new File("/Users/zhaofei/Downloads/test.png");
		if (file.isDirectory()) {
			ImageIO.write(bi, "png", file);
		} else {
			file.mkdirs();
			ImageIO.write(bi, "png", file);
		}
	}

	// @Test
	public void 更新单个菜单文本() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("更新单个菜单:");
		HttpPut put = new HttpPut(rootUrl + "/recipes/52");

		JSONObject object = new JSONObject();
		object.put("id", 52);
		object.put("name", "咖喱牛肉土豆");
		object.put("description", "咖喱牛肉土豆22");

		StringEntity entity = new StringEntity(object.toString(), "UTF-8");

		put.setEntity(entity);
		put.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse response = client.execute(put);
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine()
				.getStatusCode());
	}

	// @Test
	public void 更新单个菜单图片() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("更新单个菜单:");
		HttpPut put = new HttpPut(rootUrl + "/recipes/52");

		File file = new File("/Users/zhaofei/Downloads/menu/咖喱牛肉土豆.jpg");
		FileEntity entity = new FileEntity(file, "application/octet-stream");

		put.setEntity(entity);
		put.setHeader("Content-Type", "application/octet-stream");

		HttpResponse response = client.execute(put);
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine()
				.getStatusCode());

	}

	// @Test
	public void 删除菜单() throws JSONException, ClientProtocolException,
			IOException {
		System.out.println("删除菜单:");
		HttpDelete delete = new HttpDelete(rootUrl + "/recipes/52");

		HttpResponse response = client.execute(delete);
		Assert.assertEquals(204, response.getStatusLine().getStatusCode());
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
