package com.chihuo.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.WebResource;

public class HelloTest {

	WebResource service;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(EncryptPassword2("1234qwerasdfzxcv"));
	}

	public static String EncryptPassword2(String paramString) {
		MessageDigest localMessageDigest = null;
		byte[] arrayOfByte;
		StringBuffer localStringBuffer;
		try {
			localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.reset();
			localMessageDigest.update(paramString.getBytes("UTF-8"));
			arrayOfByte = localMessageDigest.digest();
			localStringBuffer = new StringBuffer();
			for (byte b : arrayOfByte) {
				localStringBuffer.append(Integer.toHexString(0xFF & b));

			}
			return localStringBuffer.toString().toUpperCase();
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			while (true)
				System.exit(-1);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			while (true)
				localUnsupportedEncodingException.printStackTrace();
		}
	}
}
