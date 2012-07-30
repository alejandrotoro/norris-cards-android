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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class BuscarPartidasActivity extends Activity {
	private Button btnVolver;
	private ListView lvPartidas;
	private String url = Global.getInstance().getProduction();
	private String message;
	private Resources res;
	private ProgressDialog pd;
	private String[] listaPartidas;
	private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_partidas);
        res = getResources();
        
        lvPartidas = (ListView)this.findViewById(R.id.lvPartidas);
        btnVolver = (Button)this.findViewById(R.id.btnVolver);
        
        btnVolver.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BuscarPartidasActivity.this, Dashboard.class);
				startActivity(intent);
			}
		});
        
        new ObtenerPartidas().execute("");
        pd = ProgressDialog.show(this, "Un momento","Consultando partidas activas",true, false);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_buscar_partidas, menu);
        return true;
    }
    
    public void error(String error){
		Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrar.vibrate(800);
		Toast.makeText(context, error, Toast.LENGTH_LONG).show();
	}
    
    private class ObtenerPartidas extends AsyncTask<String, Void, Object>{
    	String data = "";
    	
		@Override
		protected Object doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
		    
		    String auth = url+"/partidas.json";
		    //HttpPost httppost = new HttpPost(auth);
		    HttpGet httpget = new HttpGet(auth);
		    try {
		        //HttpResponse response = httpclient.execute(httppost);
		    	HttpResponse response = httpclient.execute(httpget);
	    		int status = response.getStatusLine().getStatusCode();
	    		
	    		if(status == 200 || status == 201){
	    			HttpEntity e = response.getEntity();
	    			data = EntityUtils.toString(e);
	    			//data = "["+data+"]";
	    			Log.i("***info", data);
	    			JSONArray a = new JSONArray(data);
	    			if(a.length() > 0){
		    			listaPartidas = new String[a.length()];
		    			
		    			for(int i = 0; i < a.length(); i++){
		    				JSONObject datos = a.getJSONObject(i);
		    				if(datos.getString("baraja_id") != null){
		    					listaPartidas[i] = "Baraja: " + datos.getString("baraja_id");
		    				}
		    			}
	    			}else{
	    				listaPartidas = null;
	    				/*message = res.getString(R.string.error_nopartidas);
	    				error(message);*/
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
		    } catch (JSONException e) {
		    	message = res.getString(R.string.connection_error);
				error(message);
			}
			
			return null;
		}
		
		protected void onPostExecute(Object result){
			pd.dismiss();
			
			if(listaPartidas == null){
				listaPartidas = new String[1];
				listaPartidas[0] = res.getString(R.string.error_nopartidas);
			}
			
			ArrayAdapter adapter = new ArrayAdapter(BuscarPartidasActivity.this, android.R.layout.simple_list_item_1, listaPartidas);
			lvPartidas.setAdapter(adapter);
			
			super.onPostExecute(result);
		}
		    	
    }

    
}
