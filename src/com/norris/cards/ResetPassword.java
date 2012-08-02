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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends Activity {

	EditText password;
	EditText v_password;
	String password_s;
	String v_password_s;
	String url = Global.getInstance().getProduction();
	
	public void goBack(View view){
		Intent intent = new Intent(ResetPassword.this, ForgotPassword.class);
		startActivity(intent);
	}
	
	public void validate(View view) throws JSONException{
		Resources res = getResources();
		String message;
		password = (EditText)this.findViewById(R.id.password_field);
		v_password = (EditText)this.findViewById(R.id.verify_password_field);
		password_s = password.getText().toString().trim();
		v_password_s = v_password.getText().toString().trim();
		if(password_s.equals("")){
			message = res.getString(R.string.password_blank);
			error(message);
		}else if(v_password_s.equals("")){
			message = res.getString(R.string.password_blank);
			error(message);
		}else if(!password_s.equals(v_password_s)){
			message = res.getString(R.string.pass_missmatch);
			error(message);
		}else {
		    HttpClient httpclient = new DefaultHttpClient();
		    
		    String auth = url+"/reset_password.json";
		    HttpPost httppost = new HttpPost(auth);
		    try {
		    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    	nameValuePairs.add(new BasicNameValuePair("user[id]", Global.getInstance().getUserId().toString()));
		    	nameValuePairs.add(new BasicNameValuePair("user[password]", password_s));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
		        HttpResponse response = httpclient.execute(httppost);
	    		int status = response.getStatusLine().getStatusCode();
	
	    		if(status == 200 || status == 201){
	    			HttpEntity e = response.getEntity();
	    			String data = EntityUtils.toString(e);
	    			JSONObject datos = new JSONObject(data);
	    			Global.getInstance().setUserId(datos.getInt("id"));
	    			if(datos.getInt("id") != 0){
	    				Global.getInstance().setUserId(datos.getInt("id"));
	    			}else{
	    				error("Correo no encontrado.");
	    			}
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
	
	public void error(String error){
		Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrar.vibrate(800);
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}
	
	public void login() throws JSONException{
		Intent intent = new Intent(ResetPassword.this, Dashboard.class);
		startActivity(intent);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
    }

}
