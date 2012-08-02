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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity {
	
	EditText email;
	String email_s;
	String url = Global.getInstance().getProduction();
	
	public void goBack(View view){
		Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void validate(View view) throws JSONException{
		Resources res = getResources();
		String message;
		email = (EditText)this.findViewById(R.id.email_field);
		email_s = email.getText().toString().trim();
		
		Global.getInstance().setUserEmail(email_s);
		
		if(email_s.equals("")){
			message = res.getString(R.string.email_blank);
			error(message);
		}else{
		    HttpClient httpclient = new DefaultHttpClient();
		    
		    String auth = url+"/verify_email.json";
		    HttpPost httppost = new HttpPost(auth);
		    try {
		    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    	nameValuePairs.add(new BasicNameValuePair("user[email]", email_s));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
		        HttpResponse response = httpclient.execute(httppost);
	    		int status = response.getStatusLine().getStatusCode();
	
	    		if(status == 200 || status == 201){
	    			HttpEntity e = response.getEntity();
	    			String data = EntityUtils.toString(e);
	    			JSONObject datos = new JSONObject(data);
	    			if(datos.getInt("id") != 0){
	    				Global.getInstance().setUserId(datos.getInt("id"));
		    			reset_pass();
	    			}else{
	    				error("Correo no encontrado.");
	    			}
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
	
	public void reset_pass() throws JSONException{
		Intent intent = new Intent(ForgotPassword.this, ResetPassword.class);
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
        setContentView(R.layout.forgot_password);
    }

}
