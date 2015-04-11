package com.xj.af.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ListViewImageTaskUtil extends AsyncTask<String, Void, Bitmap> {

	private ImageView iv;
	private LruCache<String, Bitmap> lruCache;

	public ListViewImageTaskUtil(ImageView iv) {
		this.iv = iv;
	}

	public ListViewImageTaskUtil(ImageView iv, LruCache<String, Bitmap> lruCache) {
		super();
		this.iv = iv;
		this.lruCache = lruCache;
	}



    @Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		try {
			bitmap = SimpleImageLoader.getBitmap(params[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addBitmapToMemoryCache(params[0], bitmap);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (result != null) {
			iv.setImageBitmap(result);
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			lruCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
		return lruCache.get(key);
	}

	public static class SimpleImageLoader {
		public static Bitmap getBitmap(String urlStr) throws IOException {
			Bitmap bitmap;
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return bitmap;
		}
	}

}
