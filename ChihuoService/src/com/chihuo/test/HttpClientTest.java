package com.chihuo.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.naming.java.javaURLContextFactory;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpClientTest {

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
	public void 加密测试() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		String password = "123456";

		byte[] salt = new byte[16];
		java.security.SecureRandom random = new java.security.SecureRandom();
		random.nextBytes(salt);

		MessageDigest md = null;
		md = MessageDigest.getInstance("MD5");
		md.update((password + byte2String(salt)).getBytes("UTF-8"));
		byte hash[] = md.digest();

		System.out.println("salt: " + byte2String(salt));
		System.out.println("hash: " + byte2String(hash));
	}

	private String byte2String(byte[] bytes) {
		StringBuffer localStringBuffer = new StringBuffer();
		for (byte b : bytes) {
			localStringBuffer.append(Integer.toHexString(0xFF & b));

		}
		String hashString = localStringBuffer.toString();
		return hashString;
	}

	@Test
	public void 登录测试() throws ClientProtocolException, IOException {
		HttpPost httpost = new HttpPost(
				"http://localhost:8080/ChihuoService/rest/login");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "user123"));
		nvps.add(new BasicNameValuePair("password", "123456"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		ResponseHandler<Object> handler = new ResponseHandler<Object>() {
			public Object handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				HttpEntity entity = response.getEntity();

				System.out.println("Login form get: "
						+ response.getStatusLine());
				EntityUtils.consume(entity);

				System.out.println("Post logon cookies:");
				List<Cookie> cookies = client.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}
				return null;
			}
		};

		client.execute(httpost, handler);
	}

	// @Test
	public void 获取所有离线数据() throws ClientProtocolException, IOException,
			JSONException {
		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();

		// Create local HTTP context
		HttpContext localContext = new BasicHttpContext();

		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		HttpGet httpget = new HttpGet(
				"http://localhost:8080/ChihuoService/rest/login");

		System.out.println("executing request " + httpget.getURI());

		// Pass loctal context as a parameter
		HttpResponse response = client.execute(httpget, localContext);
		HttpEntity entity = response.getEntity();

		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());
		if (entity != null) {
			System.out.println("Response content length: "
					+ entity.getContentLength());
		}

		List<Cookie> cookies = cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			System.out.println("Local cookie: " + cookies.get(i));
		}

		// Consume response content
		EntityUtils.consume(entity);

		System.out.println("----------------------------------------");
	}
}
