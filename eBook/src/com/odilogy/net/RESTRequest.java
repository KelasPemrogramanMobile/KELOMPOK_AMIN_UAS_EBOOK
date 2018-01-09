package com.odilogy.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.odilogy.ebookstore.R;

import android.content.Context;
import android.util.Log;

/**
 * RESTRequest holds http request functionality
 * @author odilogy@gmail.com
 * */
public class RESTRequest {
	private String HOST;
	private String BASE_API_URL;
	private Context mContext;
	private String USERNAME;
	private String PASSWORD;
	private String BASEPATHURL;
	
	/**
	 * Constructor
	 * @param Context application context
	 * @param HOST host server such www.google.com, 192.168.2.89 (without protocol)
	 * @param BASE_PATH path of the api  for example www.google.com/api/  so the BASE_API_URL if the HOST is
	 * www.google.com is "api/"
	 * */
	public RESTRequest(Context context, String HOST, String BASE_API_URL) {
		this.HOST = HOST;
		this.BASE_API_URL = "http://"+HOST+BASE_API_URL;
		this.mContext = context;
		this.BASEPATHURL = this.BASE_API_URL;
		this.USERNAME = context.getString(R.string.API_USERNAME);
		this.PASSWORD = context.getString(R.string.API_PASSWORD);
	}
	
	/**
	 * getAllBook -> get all latest book order by date desc
	 * @return {@link JSONObject}
	 * */
	public JSONObject getAllBook(){
		Object response = makeRequest("book");
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObj;
	}
	
	/**
	 * get all available category
	 * @return {@link JSONObject}
	 * */
	public JSONObject getAllCategory(){
		Object response = makeRequest("category");
		Log.v("RESPONSE ON REQ", response.toString());
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObj;
	}
	
	/**
	 * get book by specific category
	 * @return {@link JSONObject}
	 * */
	public JSONObject getBookByCategory(int category_id){
		Object response = makeRequest("book/category_id/"+category_id);
		Log.v("RESPONSE ON REQ", response.toString());
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObj;
	}
	
	/**
	 * get book by search certain keyword
	 * @return {@link JSONObject}
	 * */
	public JSONObject getBookBySearch(String keyword){
		Object response = makeRequest("search/q/"+keyword);
		Log.v("RESPONSE ON REQ", response.toString());
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObj;
	}
	
	/**
	 * make request dpends on api url
	 * @return {@link JSONObject}
	 * */
	private Object makeRequest(String url) {
		InputStream is = null;
		String js_string = "";
		try {
			HttpClient client = new DefaultHttpClient();
			AuthScope as = new AuthScope(HOST, 80);
			UsernamePasswordCredentials upc = new UsernamePasswordCredentials(
					USERNAME, PASSWORD);
			((AbstractHttpClient) client).getCredentialsProvider()
					.setCredentials(as, upc);
			BasicHttpContext localContext = new BasicHttpContext();
			BasicScheme basicAuth = new BasicScheme();
			localContext.setAttribute("preemptive-auth", basicAuth);
			HttpHost targetHost = new HttpHost(HOST, 80, null);
			HttpGet httpget = new HttpGet(BASEPATHURL + url);
			Log.v("SEARCH URL", BASEPATHURL + url);
			HttpParams params = httpget.getParams();
			httpget.setHeader("Content-Type", "application/json");
			HttpResponse response = client.execute(targetHost, httpget,
					localContext);
			HttpEntity entity = response.getEntity();
			// Object content = EntityUtils.toString(entity);
			is = entity.getContent();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				js_string = sb.toString();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			// Log.d(MY_APP_TAG, "OK: " + content.toString());
			return js_string;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
