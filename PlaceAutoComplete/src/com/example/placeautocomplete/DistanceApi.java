package com.example.placeautocomplete;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DistanceApi extends AsyncTask<String, Integer, ArrayList<String>> {

	/*https://maps.googleapis.com/maps/api/directions/json?origin=chennai&destination=madurai&sensor=false
*/	
	private static final String LOG_TAG = "DistanceAPI";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/directions";
	private static final String OUT_JSON = "/json";
	private static final String ORIGIN = "?origin=";
	private static final String DESTINATION = "&destination=";
	private static final String SENSOR = "&sensor=false";
	private ArrayList<String> result;
	private HttpURLConnection conn;
	private StringBuilder jsonResults;
	private TextView durationText,distanceText;
	
	public DistanceApi(Context context) {
		distanceText = (TextView)((PlaceActivity)context).findViewById(R.id.distance);
		durationText = (TextView)((PlaceActivity)context).findViewById(R.id.duration);
	}
	
	@Override
	protected void onPreExecute() {
		result = new ArrayList<String>();
		conn = null;
		jsonResults = new StringBuilder();
	}

	@Override
	protected ArrayList<String> doInBackground(String... inputs) {
			
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + OUT_JSON);
			sb.append(ORIGIN + URLEncoder.encode(inputs[0], "utf8"));
			sb.append(DESTINATION + URLEncoder.encode(inputs[1], "utf8"));
			sb.append(SENSOR);
			
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			
			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return result;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return result;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			/*Log.d("JSON", jsonResults.toString());*/
			JSONArray routesJsonArray = jsonObj.getJSONArray("routes");
			JSONArray legsJsonArray = routesJsonArray.getJSONObject(0).getJSONArray("legs");
			// Extract the Place descriptions from the results
				
				// store Killometer in Distance
				JSONObject path = legsJsonArray.getJSONObject(0);
				JSONObject distance =  path.getJSONObject("distance");
				JSONObject duration =  path.getJSONObject("duration");
				result.add(distance.getString("text"));
				result.add(duration.getString("text"));
			
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}
		
		
		return result;
	}

	@Override
	protected void onPostExecute(ArrayList<String> distance) {
		for (String string : distance) {
			Log.d("DISTANCE API OUTPUT",string);
		}
		distanceText.setText(distance.get(0));
		durationText.setText(distance.get(1));
		
	}

}
