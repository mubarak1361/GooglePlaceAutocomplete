package com.example.placeautocomplete;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebServiceHandler {

	static String response = null;
	public static int TIMEOUT = 1000 * 2;
	public final static int GET = 1;
	public final static int POST = 2;
	public final static int PUT = 3;
	public final static int DELETE = 4;
	private static DefaultHttpClient httpClient;
	private static WebServiceHandler serviceHandler;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpDelete httpDelete;
	private HttpPut httpPut;
	private JSONObject JSONresponseText;

	public WebServiceHandler() {

	}

	public static WebServiceHandler Instance() {
		if (serviceHandler == null) {
			serviceHandler = new WebServiceHandler();
			httpClient = new DefaultHttpClient();
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
		}
		return serviceHandler;
	}

	// GET METHOD
	public JSONObject ServiceGET(String url) {
		return this.makeServiceCall(url, GET, null);
	}

	// POST METHOD
	public JSONObject ServicePOST(String url, JSONObject object) {
		return this.makeServiceCall(url, POST, object);
	}

	// PUT METHOD
	public JSONObject ServicePUT(String url, JSONObject object) {
		return this.makeServiceCall(url, PUT, object);
	}

	// DELETE METHOD
	public JSONObject ServiceDELETE(String url) {
		return this.makeServiceCall(url, DELETE, null);
	}

	public JSONObject makeServiceCall(String url, int method, JSONObject object) {
		try {

			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			if (method == GET) {

				httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);

			}

			// Checking http request method type
			if (method == POST) {

				httpPost = new HttpPost(url);
				// adding post params
				if (object != null) {

					httpPost.setEntity(new ByteArrayEntity(object.toString()
							.getBytes("UTF8")));
					httpResponse = httpClient.execute(httpPost);
				}

			}

			if (method == PUT) {

				httpPut = new HttpPut(url);
				// adding post params
				if (object != null) {

					httpPut.setEntity(new ByteArrayEntity(object.toString()
							.getBytes("UTF8")));
					httpResponse = httpClient.execute(httpPut);
				}

			}

			if (method == DELETE) {

				httpDelete = new HttpDelete(url);
				httpResponse = httpClient.execute(httpDelete);

			}

			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			JSONresponseText = new JSONObject(response);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("JSON Parser", "ERROR: " + e.toString());
		}

		return JSONresponseText;

	}

}
