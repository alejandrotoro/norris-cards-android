package com.norris.cards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText email;
	EditText password;
	String email_s;
	String password_s;
	String url = Global.getInstance().getDevelopment();
	
	public void validate(View view) throws JSONException{
		Resources res = getResources();
		String message;
		email = (EditText)this.findViewById(R.id.email_field);
		password = (EditText)this.findViewById(R.id.password_field);
		email_s = email.getText().toString().trim();
		password_s = password.getText().toString().trim();
		
		Global.getInstance().setUserEmail(email_s);
		
		if(email_s.equals("")){
			message = res.getString(R.string.email_blank);
			error(message);
		}else if(password_s.equals("")){
			message = res.getString(R.string.password_blank);
			error(message);
		}else{
			/** login(email_s); */
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    
		    String auth = url+"/users/sign_in";
		    HttpPost httppost = new HttpPost(auth);
		    try {
		    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    	nameValuePairs.add(new BasicNameValuePair("user[email]", email_s));
		    	nameValuePairs.add(new BasicNameValuePair("user[password]", password_s));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
	    		int status = response.getStatusLine().getStatusCode();
	
	    		if(status == 200){
	    			/**
	    			HttpEntity e = response.getEntity();
	    			String data = EntityUtils.toString(e);
	    			data = "["+data+"]";
	    			JSONArray a = new JSONArray(data);
	    			JSONObject datos = a.getJSONObject(0);
	    			**/
	    			login();
	    		}else {
	    			HttpEntity e = response.getEntity();
	    			String data = EntityUtils.toString(e);
					error(data);
	    		}
		        
		    } catch (ClientProtocolException e) {
				message = res.getString(R.string.connection_error);
				error(message);
		    } catch (IOException e) {
				message = res.getString(R.string.connection_error);
				error(message);
		    }
		}
	}
	
	public void login() throws JSONException{
		Intent intent = new Intent(MainActivity.this, Dashboard.class);
		startActivity(intent);
	}
	
	public void error(String error){
		Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrar.vibrate(800);
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}