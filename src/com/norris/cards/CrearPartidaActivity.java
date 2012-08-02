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

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class CrearPartidaActivity extends Activity {
	private Button btnVolver; 
	private Spinner spNumeroJugadores;
	private String[] numeroJugadores = {"3", "4", "5", "6", "7"};
	private Button btnGuardar;
	private String url = Global.getInstance().getProduction();
	private String message;
	private Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_partida);
        
        btnVolver = (Button)this.findViewById(R.id.btnVolver);
        btnGuardar = (Button)this.findViewById(R.id.btnGuardar);
        spNumeroJugadores = (Spinner)this.findViewById(R.id.spNumeroJugadores);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, numeroJugadores);
        spNumeroJugadores.setAdapter(adapter);
        
        btnVolver.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CrearPartidaActivity.this, Dashboard.class);
				startActivity(intent);
			}
		});
        
        btnGuardar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String numJugadores = spNumeroJugadores.getSelectedItem().toString();
				String usuario = ""+Global.getInstance().getUserId();
				String baraja = ""+Global.getInstance().getBarajaId();
				
				HttpClient httpclient = new DefaultHttpClient();
			    
			    String auth = url+"/partidas.json";
			    HttpPost httppost = new HttpPost(auth);
			    
			    try {
			    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    	nameValuePairs.add(new BasicNameValuePair("partida[baraja_id]", baraja));
			    	nameValuePairs.add(new BasicNameValuePair("partida[cantidad_jugadores]", numJugadores));
			    	nameValuePairs.add(new BasicNameValuePair("partida[creador_id]", usuario));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
			        HttpResponse response = httpclient.execute(httppost);
		    		int status = response.getStatusLine().getStatusCode();
		
		    		if(status == 200 || status == 201){
		    			HttpEntity e = response.getEntity();
		    			String data = EntityUtils.toString(e);
		    			data = "["+data+"]";
		    			JSONArray a = new JSONArray(data);
		    			JSONObject datos = a.getJSONObject(0);
		    			
		    			Global.getInstance().setPartidaId(datos.getInt("id"));
		    			Intent intent = new Intent(CrearPartidaActivity.this, InicioPartidaActivity.class);
		    			startActivity(intent);
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
			    } catch (JSONException e) {
			    	message = res.getString(R.string.connection_error);
					error(message);
				}catch (Exception e) {
			    	message = res.getString(R.string.connection_error);
					error(message);
				}
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_crear_partida, menu);
        return true;
    }
    
    public void error(String error){
		Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrar.vibrate(800);
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}

    
}
