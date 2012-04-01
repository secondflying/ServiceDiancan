package com.chihuo.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpClientCategoryTest {

	DefaultHttpClient client;
	HttpClient cachingClient;
	String rootUrl = "http://localhost.:8080/ChihuoService/rest";

	@Before
	public void setUp() throws Exception {
		client = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("localhost", 8888);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		CacheConfig cacheConfig = new CacheConfig();
		cacheConfig.setMaxCacheEntries(1000);
		cacheConfig.setMaxObjectSizeBytes(8192);
		cacheConfig.setSharedCache(false);

		cachingClient = new CachingHttpClient(client, cacheConfig);
	}

	@After
	public void tearDown() throws Exception {
		client.getConnectionManager().shutdown();
	}

	// @Test
	public void 获取所有类别() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("所有类别:");
		HttpGet get = new HttpGet(rootUrl + "/categories");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));
	}

	// @Test
	public void 获取单个类别() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("获取单个类别:");
		HttpGet get = new HttpGet(rootUrl + "/categories/45");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = client.execute(get);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));
	}

	@Test
	public void 测试缓存1() throws ClientProtocolException, IOException,
			JSONException {
//		测试缓存();
//		test();
	}

	@Test
	public void 测试缓存2() throws ClientProtocolException, IOException,
			JSONException {
//		测试缓存();
//		test();
	}

	public void 测试缓存() throws ClientProtocolException, IOException,
			JSONException {

		HttpContext localContext = new BasicHttpContext();

		System.out.println("获取单个类别:");
		HttpGet get = new HttpGet(rootUrl + "/categories/45");
		get.addHeader("accept", "application/json;charset=UTF-8");
		get.addHeader("Accept-Charset", "utf-8");
		HttpResponse response = cachingClient.execute(get, localContext);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();
		System.out.println(entity.getContentType().getValue());

		System.out.println(showContent(entity.getContent()));

		EntityUtils.consume(entity);
		CacheResponseStatus responseStatus = (CacheResponseStatus) localContext
				.getAttribute(CachingHttpClient.CACHE_RESPONSE_STATUS);
		switch (responseStatus) {
		case CACHE_HIT:
			System.out
					.println("A response was generated from the cache with no requests sent upstream");
			break;
		case CACHE_MODULE_RESPONSE:
			System.out
					.println("The response was generated directly by the caching module");
			break;
		case CACHE_MISS:
			System.out.println("The response came from an upstream server");
			break;
		case VALIDATED:
			System.out
					.println("The response was generated from the cache after validating "
							+ "the entry with the origin server");
			break;
		}
	}

	public void test() throws ClientProtocolException, IOException{
		CacheConfig cacheConfig = new CacheConfig();  
		cacheConfig.setMaxCacheEntries(1000);
		cacheConfig.setMaxObjectSizeBytes(819200);

		HttpClient cachingClient = new CachingHttpClient(client, cacheConfig);

		HttpContext localContext = new BasicHttpContext();
		HttpGet httpget = new HttpGet("http://gimg.baidu.com/img/gs.gif");
		HttpResponse response = cachingClient.execute(httpget, localContext);
		HttpEntity entity = response.getEntity();
		EntityUtils.consume(entity);
		CacheResponseStatus responseStatus = (CacheResponseStatus) localContext.getAttribute(
		        CachingHttpClient.CACHE_RESPONSE_STATUS);
		switch (responseStatus) {
		case CACHE_HIT:
		    System.out.println("A response was generated from the cache with no requests " +
		            "sent upstream");
		    break;
		case CACHE_MODULE_RESPONSE:
		    System.out.println("The response was generated directly by the caching module");
		    break;
		case CACHE_MISS:
		    System.out.println("The response came from an upstream server");
		    break;
		case VALIDATED:
		    System.out.println("The response was generated from the cache after validating " +
		            "the entry with the origin server");
		    break;
		}
	}
	
	// @Test
	public void 新增种类() throws ClientProtocolException, IOException,
			JSONException {
		System.out.println("新增种类:");
		// String[] categories = { "热菜", "凉菜", "汤煲", "糕点小吃", "茶酒饮品" };
		String[] categories = { "火锅2", "海鲜2" };
		for (String category : categories) {
			HttpPost post = new HttpPost(rootUrl + "/categories");

			JSONObject object = new JSONObject();
			object.put("name", category);
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
			// java.lang.IllegalStateException: Invalid use of
			// SingleClientConnManager: connection still allocated.
			// Make sure to release the connection before allocating another
			// one.
			EntityUtils.consume(response.getEntity());
		}
	}

	// @Test
	public void 新增种类2() throws JSONException, ClientProtocolException,
			IOException {
		System.out.println("新增菜单:");
		HttpPost post = new HttpPost(rootUrl + "/categories");

		String dic = "/develop/project/chihuo/svn/trunk/测试数据/category/";
		File dictory = new File(dic);
		if (dictory.isDirectory()) {
			File[] files = dictory.listFiles();
			for (File imgFile : files) {
				if (imgFile.isFile() && !imgFile.isHidden()) {
					String filename = imgFile.getName();
					String name = filename.substring(0,
							filename.lastIndexOf('.'));

					JSONObject object = new JSONObject();
					object.put("name", name);

					StringEntity entity = new StringEntity(object.toString(),
							"UTF-8");
					entity.setContentType("application/json");
					entity.setContentEncoding("UTF-8");

					StringBody recipeBody = new StringBody(object.toString(),
							"application/json", Charset.forName("UTF-8"));
					FileBody imageBody = new FileBody(imgFile,
							"application/octet-stream");

					MultipartEntity reqEntity = new MultipartEntity();
					reqEntity.addPart("recipe", recipeBody);
					reqEntity.addPart("recipeImage", imageBody);

					post.setEntity(reqEntity);

					HttpClientParams.setRedirecting(post.getParams(), false);
					HttpResponse response = client.execute(post);
					Assert.assertEquals(201, response.getStatusLine()
							.getStatusCode());

					String location = response.getLastHeader("Location")
							.getValue();
					System.out.println("创建成功：" + location);

					EntityUtils.consume(response.getEntity());
					// break;
				}
			}
		}
	}

	// @Test
	public void 更新种类() throws JSONException, ClientProtocolException,
			IOException {
		System.out.println("更新种类:");
		HttpPut put = new HttpPut(rootUrl + "/categories/23");

		JSONObject object = new JSONObject();
		object.put("name", "大菜");
		System.out.println(object.toString());

		StringEntity entity = new StringEntity(object.toString(), "UTF-8");
		entity.setContentType("application/json;charset=UTF-8");
		entity.setContentEncoding("UTF-8");

		put.setEntity(entity);
		put.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpClientParams.setRedirecting(put.getParams(), false);

		HttpResponse response = client.execute(put);
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine()
				.getStatusCode());
	}

	// @Test
	public void 删除种类() throws JSONException, ClientProtocolException,
			IOException {
		System.out.println("新增种类:");
		HttpDelete delete = new HttpDelete(rootUrl + "/categories/25");

		HttpResponse response = client.execute(delete);
		Assert.assertEquals(204, response.getStatusLine().getStatusCode());
	}

	// @Test
	public void 获取种类图片() throws ClientProtocolException, IOException {
		System.out.println("获取种类图片:");
		HttpGet get = new HttpGet(rootUrl + "/categories/45");
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
