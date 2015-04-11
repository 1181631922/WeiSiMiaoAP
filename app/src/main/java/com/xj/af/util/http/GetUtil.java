package com.xj.af.util.http;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GetUtil {
	public static String get(String url) throws ParseException, IOException {
		String strResult = "";
		HttpGet httpRequest = new HttpGet(url);

		HttpResponse httpResponse = new DefaultHttpClient()
				.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			strResult = EntityUtils.toString(httpResponse.getEntity());
		}

		return strResult;
	}
}
