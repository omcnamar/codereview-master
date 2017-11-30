package com.intelematics.interview.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.JsonReader;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 *
 */
public class ConnectionManager {
	private HttpURLConnection httpConnection = null;
	private URL url = null;
	private InputStream is = null;
	private JsonReader jsonReader = null;
	
	private Context context;
	
	
	public ConnectionManager(Context context, String requestURL){
		this.context = context;
		
		try {
			url = new URL(requestURL);
			
		} catch (MalformedURLException e) {
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	
	public JsonReader requestJson(){
		Log.d("abc", "doInBackground: " + "2");

		try {
			Log.d("abc", "doInBackground: " + "3");
			jsonReader = new JsonReader(new InputStreamReader(request(), "UTF-8"));
			Log.d("abc", "doInBackground: " + "11");
		} catch (UnsupportedEncodingException e) {
		}
		Log.d("abc", "doInBackground: " + "12");

		return jsonReader;
	}
	
	public InputStream request(){

		Log.d("abc", "doInBackground: " + "4");
		try {
			Log.d("abc", "doInBackground: " + "5");
			httpConnection = (HttpURLConnection) url.openConnection();
			Log.d("abc", "doInBackground: " + "6 " +  httpConnection);
			Log.d("abc", "doInBackground: " + httpConnection.getResponseCode());
			int responseCode = httpConnection.getResponseCode();
			Log.d("abc", "doInBackground: " + "7");
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Log.d("abc", "doInBackground: " + "8");
				is = httpConnection.getInputStream();
				Log.d("abc", "doInBackground: " + "9");
			}
	        
	    } catch (Exception ex) {
			Log.d("abc", "exception: " + ex);
		}
		Log.d("abc", "doInBackground: " + "10 " +  is);

		return is;
	}
	
	public void closeConnection(){
	    try{
	    	if(is != null){
	    		is.close();
	    	}
	    	if(httpConnection != null){
	    		httpConnection.disconnect();
	    	}
		} catch(Exception e){
		}
	}
	
	
	public ByteArrayBuffer requestImage(){
		HttpURLConnection httpConnection = null;
		ByteArrayBuffer baf = new ByteArrayBuffer(1024);
		BufferedInputStream bis = null;

		if(!isNetworkAvailable()){
			return null;
		}
		
	    try {
	        httpConnection = (HttpURLConnection) url.openConnection();
	        
	        int responseCode = httpConnection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	        	bis = new BufferedInputStream(httpConnection.getInputStream(), 1024);

				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
	            
	        } 
	        
	    } catch (Exception ex) {

	    }
	    return baf;
	} 
	
}
