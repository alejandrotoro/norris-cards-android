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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class UserSettings extends ListActivity implements OnClickListener {

	public OnLongClickListener longClickListner;
	EditText password, v_password;
	String password_s, v_password_s;
	String url = Global.getInstance().getProduction();
	LinearLayout change_pass, estadisticas, top_10;
	TextView change_pass_lbl, estadisticas_lbl, top_10_lbl, logout_lbl;
	View openLayout;
	
	public void goBack(View view){
		Intent intent = new Intent(UserSettings.this, Dashboard.class);
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
	
	public void login() throws JSONException, ClientProtocolException, IOException{
		Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrar.vibrate(800);
		Toast.makeText(this, "Tu contrase–a ha sido modificada con Žxito.", Toast.LENGTH_LONG).show();

	    HttpClient httpclient = new DefaultHttpClient();
	    String auth = url+"/users/sign_out.json";
	    HttpDelete httpdelete = new HttpDelete(auth);
	    httpclient.execute(httpdelete);
		
		Intent intent = new Intent(UserSettings.this, MainActivity.class);
		startActivity(intent);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
        
        /* Definicion de los layouts del contenido */
        change_pass = (LinearLayout) findViewById(R.id.change_pass);
        estadisticas = (LinearLayout) findViewById(R.id.estadisticas);
        top_10 = (LinearLayout) findViewById(R.id.top_10);
        
        /* Definicion de los encabezados de cada item */
        change_pass_lbl = (TextView) findViewById(R.id.change_pass_lbl);
        estadisticas_lbl = (TextView) findViewById(R.id.estadisticas_lbl);
        top_10_lbl = (TextView) findViewById(R.id.top_10_lbl);
        logout_lbl = (TextView) findViewById(R.id.logout_lbl);
        
        /* Convertir en objeto clickeable cada encabezado */
        change_pass_lbl.setOnClickListener(this);
        estadisticas_lbl.setOnClickListener(this);
        top_10_lbl.setOnClickListener(this);
        logout_lbl.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	hideOthers(v);
    }


    private void hideThemAll() {
    	if(openLayout == null) return;
    	if(openLayout == change_pass)
    		change_pass.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, change_pass, true));
    	if(openLayout == estadisticas)
    		estadisticas.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, estadisticas, true));
    	if(openLayout == top_10)
    		top_10.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, top_10, true));
    }

    private void hideOthers(View layoutView) {
    	int v;
		Resources res = getResources();
		String message;
    	if(layoutView.getId() == R.id.change_pass_lbl) {
    		v = change_pass.getVisibility();
    		if(v != View.VISIBLE) {
    			change_pass.setVisibility(View.VISIBLE);
    		}
    		hideThemAll();
    		if(v != View.VISIBLE) {
    			change_pass.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, change_pass, true));
    		}
    	} else if(layoutView.getId() == R.id.estadisticas_lbl) {
    		v = estadisticas.getVisibility();
    		hideThemAll();
    		if(v != View.VISIBLE) {
    			estadisticas.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, estadisticas, true));
    		}
    	} else if(layoutView.getId() == R.id.top_10_lbl) {
    		v = top_10.getVisibility();
    		hideThemAll();
    		if(v != View.VISIBLE) {
    			top_10.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, top_10, true));
    		}
    	} else if(layoutView.getId() == R.id.logout_lbl) {
    		hideThemAll();
    	    HttpClient httpclient = new DefaultHttpClient();
    	    String auth = url+"/users/sign_out.json";
    	    HttpDelete httpdelete = new HttpDelete(auth);
    	    try {
				httpclient.execute(httpdelete);
			} catch (ClientProtocolException e) {
				message = res.getString(R.string.connection_error);
				error(message);
			} catch (IOException e) {
				message = res.getString(R.string.connection_error);
				error(message);
			}
    		
    		Intent intent = new Intent(UserSettings.this, MainActivity.class);
    		startActivity(intent);
    	}
    }
    
    public class ScaleAnimToHide extends ScaleAnimation {
    	private View mView;
    	private LayoutParams mLayoutParams;
    	private int mMarginBottomFromY, mMarginBottomToY;
    	private boolean mVanishAfter = false;
    	public ScaleAnimToHide(float fromX, float toX, float fromY, float toY, int duration, View view,boolean vanishAfter) {
    		super(fromX, toX, fromY, toY);
    		setDuration(duration);
    		openLayout = null;
    		mView = view;
    		mVanishAfter = vanishAfter;
    		mLayoutParams = (LayoutParams) view.getLayoutParams();
    		int height = mView.getHeight();
    		mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
    		mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;
    	}
    	@Override
    	protected void applyTransformation(float interpolatedTime, Transformation t) {
    		super.applyTransformation(interpolatedTime, t);
    		if (interpolatedTime < 1.0f) {
    			int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
    			mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
    			mView.getParent().requestLayout();
    		}
    		else if (mVanishAfter) {
    			mView.setVisibility(View.GONE);
    		}
    	}
    }
    
    public class ScaleAnimToShow extends ScaleAnimation {
    	private View mView;
    	private LayoutParams mLayoutParams;
    	private int mMarginBottomFromY, mMarginBottomToY;
    	private boolean mVanishAfter = false;

    	public ScaleAnimToShow(float toX, float fromX, float toY, float fromY, int duration, View view,boolean vanishAfter) {
    		super(fromX, toX, fromY, toY);
    		openLayout = view;
    		setDuration(duration);
    		mView = view;
    		mVanishAfter = vanishAfter;
    		mLayoutParams = (LayoutParams) view.getLayoutParams();
    		mView.setVisibility(View.VISIBLE);
    		int height = mView.getHeight();
    		mMarginBottomFromY = 0;
    		mMarginBottomToY = height;
    	}
    	@Override
    	protected void applyTransformation(float interpolatedTime, Transformation t) {
    		super.applyTransformation(interpolatedTime, t);
    		if (interpolatedTime < 1.0f) {
    			int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
    			mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
    			mView.getParent().requestLayout();
    		}
    	}
    }
    
}
