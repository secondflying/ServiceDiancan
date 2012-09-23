package com.chihuo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
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

//	@Test
//	public void 加密测试() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
//		String password = "123456";
//
//		byte[] salt = new byte[16];
//		java.security.SecureRandom random = new java.security.SecureRandom();
//		random.nextBytes(salt);
//
//		MessageDigest md = null;
//		md = MessageDigest.getInstance("MD5");
//		md.update((password + byte2String(salt)).getBytes("UTF-8"));
//		byte hash[] = md.digest();
//
//		System.out.println("salt: " + byte2String(salt));
//		System.out.println("hash: " + byte2String(hash));
//	}
//
//	private String byte2String(byte[] bytes) {
//		StringBuffer localStringBuffer = new StringBuffer();
//		for (byte b : bytes) {
//			localStringBuffer.append(Integer.toHexString(0xFF & b));
//
//		}
//		String hashString = localStringBuffer.toString();
//		return hashString;
//	}

	@Test
	public void 登录测试() throws ClientProtocolException, IOException {
		HttpPost httpost = new HttpPost("http://210.72.21.40/survey/gate.php");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("salt", "OSwyMCwxMSw0Niw1Nyw2MywxMDAsOTYsOTc0Miw="));
		nvps.add(new BasicNameValuePair("hash", "8119e1c3ae7e59855513a97619f02e3e0584b1d8_3"));
		nvps.add(new BasicNameValuePair("submit", ""));
		httpost.setEntity(new UrlEncodedFormEntity(nvps));
		
		CookieStore cookieStore = new BasicCookieStore();
	    HttpContext localContext = new BasicHttpContext();
	    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
	    
		httpost.addHeader("Origin", "http://www.china.com.cn");
		httpost.addHeader("Referer", "http://www.china.com.cn/119/static/q//02e8e3bfdbd09f9f090f2355bcc722b6b5b19578_5.html");
		httpost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");

		HttpClientParams.setRedirecting(httpost.getParams(), true);
	
		HttpResponse response = client.execute(httpost);
		
		HttpEntity responseEntity = response.getEntity();
		
		System.out.println("状态码：");
		System.out.println(response.getStatusLine().getStatusCode());
		
		String jsonString=parseContent(responseEntity.getContent());
		
		System.out.println("返回的参数：");
		System.out.println(jsonString);
		
		EntityUtils.consume(response.getEntity());

	}
	
	private static String parseContent(InputStream stream) throws IOException {
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
