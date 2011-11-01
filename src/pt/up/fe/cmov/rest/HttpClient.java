package pt.up.fe.cmov.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpClient {
	private static final String TAG = "HttpClient";

	public static JSONObject SendHttpGet(String url) throws ConnectTimeoutException {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		
		HttpGet request = new HttpGet(url);
		initRequest(request);
		String res = executeRequest(client, request);
		if (res == null) throw new ConnectTimeoutException();
		return stringToJSONObject(res);
	}
	
	public static JSONArray SendHttpGetArray(String url) throws ConnectTimeoutException  {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		HttpGet request = new HttpGet(url);
		initRequest(request);
		String res = executeRequest(client, request);
		if (res == null) throw new ConnectTimeoutException();
		return stringToJSONArray(res);
	}

	public static JSONObject SendHttpPost(String url, JSONObject objSend) throws ConnectTimeoutException  {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		initRequest(request);
		setSendObject(request, objSend);
		String res = executeRequest(client, request);
		if (res == null) throw new ConnectTimeoutException();
		return stringToJSONObject(res);	}
	
	public static JSONObject SendHttpDelete(String url) throws ConnectTimeoutException  {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(url);

		initRequest(request);
		String res = executeRequest(client, request);
		if (res == null) throw new ConnectTimeoutException();
		return stringToJSONObject(res);
	}

	public static JSONObject SendHttpPut(String url, JSONObject objSend) throws ConnectTimeoutException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(url);

		initRequest(request);
		setSendObject(request, objSend);
		String res = executeRequest(client, request);
		if (res == null) throw new ConnectTimeoutException();
		return stringToJSONObject(res);
	}
	
	private static void initRequest(HttpRequestBase request) {
		//send/receive data is JSON 
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		//uses GZip for enconding
		request.setHeader("Accept-Encoding", "gzip");
	}
	
	private static String handleResponse(HttpResponse response) {
		if (response == null)
			return null;
		try {
			HttpEntity entity = response.getEntity();
	
			if (entity != null) {
				//decodes stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}
	
				String resultString = convertStreamToString(instream);
				instream.close();
								
				return resultString;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private static String executeRequest(DefaultHttpClient client, HttpRequestBase request) {
		if (client == null || request == null)
			return null;
		try {
			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) client.execute(request);
			Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");
			return handleResponse(response);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static JSONObject stringToJSONObject(String str) {
		
		if (str == null)
			return null;
		
		try {
			JSONObject obj = new JSONObject(str);
			Log.i(TAG, "<jsonobject>\n" + obj.toString() + "\n</jsonobject>");
			return obj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}

	private static JSONArray stringToJSONArray(String str) {
		
		if (str == null)
			return new JSONArray();
		
		try {
			JSONArray obj = new JSONArray(str);
			Log.i(TAG, "<jsonobject>\n" + obj.toString() + "\n</jsonobject>");
			return obj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	private static void setSendObject(HttpEntityEnclosingRequestBase request, JSONObject obj) {
		try {
			StringEntity se = new StringEntity(obj.toString());
			request.setEntity(se);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 * 
		 * (c) public domain:
		 * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/
		 * 11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}