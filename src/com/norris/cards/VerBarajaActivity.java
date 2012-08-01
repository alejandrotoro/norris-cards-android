package com.norris.cards;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VerBarajaActivity extends Activity {
	
	public LinkedList<String[][]> lstbarajas;//Se define la lista de barajas
	public String url = Global.getInstance().getProduction();
	String data = "";
	private String[][] listaBarajas;
	private int[] idsBarajas;
	private String message;
	private Resources res;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_baraja);
        
        obtenerBarajas();//Se obtienen las barajas
		//ListView l = (ListView) findViewById(R.id.ListView01);
		//l.setAdapter(new miAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ver_baraja, menu);
        return true;
    }
    
    private static class miAdapter extends BaseAdapter {
    	 
		private LayoutInflater mInflater;
		private ImageManager imageManager;
		private Context actividad;
 
		private static final String[][] datos = {{"Carros","1","http://colombia.golgolgol.net/images_repository/0/9404_escudo.png_fd919ee6196ded3f4983b4ee2fa91bb4.png"}, 
												   {"Chicas exoticas","2","http://i.imgur.com/LHJiA.jpg"}, 
												   {"Choferes","3","http://i.imgur.com/VsVMQ.jpg"}, 
												   {"Futbolistas","4","http://colombia.golgolgol.net/images_repository/0/9404_escudo.png_fd919ee6196ded3f4983b4ee2fa91bb4.png"}, 
												   {"Presentadoras","5","http://i.imgur.com/LHJiA.jpg"}, 
												   {"Sexo","5","http://i.imgur.com/VsVMQ.jpg"}, 
												   {"Amor","5","http://i.imgur.com/LHJiA.jpg"}, 
												   {"Tecnología","5","http://i.imgur.com/VsVMQ.jpg"}};
 
		public miAdapter(Context context) {
 
			mInflater = LayoutInflater.from(context);
		    imageManager = new ImageManager(context);
		    actividad = context;
 
		}
 
		public View getView(int position, View convertView, ViewGroup parent) {
 
			TextView text;
			ImageView imgBaraja;
			ImageView imgNext;
 
			if (convertView == null) {
 
				convertView = mInflater.inflate(R.layout.lista_barajas, null);
 
			} 
 
			text = (TextView) convertView.findViewById(R.id.lblnombre_baraja);
			imgBaraja = (ImageView) convertView.findViewById(R.id.imgbajara);
			imgNext = (ImageView) convertView.findViewById(R.id.imgnext);
 
			text.setText(datos[position][0]);
			
			//Se muestra la imagen de la bajara que se trae desde el servidor
	        imageManager.fetchDrawableOnThread(datos[position][2], imgBaraja);
 
	        imgNext.setId(Integer.parseInt(datos[position][1]));
	        imgNext.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(actividad, "eee id: "+v.getId(),Toast.LENGTH_SHORT).show();
				}
			});
	        
			return convertView;
		}
 
 
		public int getCount() {
 
			return datos.length;
 
		}
 
 
		public Object getItem(int position) {
 
			return position;
 
		}
 
		public long getItemId(int position) {
 
			return position;
 
		}
	}
    
	 //Obtener datos barajas
    /**
     * M�todo para obtener las barajas desde el servidor y meterlas en un array
     * 
     */
    private void obtenerBarajas()
    {
    	res = getResources();
		HttpClient httpclient = new DefaultHttpClient();
	    
	    //String auth = url+"/barajas.json";
	    String auth = "http://norris-cards.herokuapp.com/barajas.json";
	    HttpGet httpget = new HttpGet(auth);
	    try {
	    	HttpResponse response = httpclient.execute(httpget);
    		int status = response.getStatusLine().getStatusCode();   		
    		if(status == 200 || status == 201){
    			HttpEntity e = response.getEntity();
    			String data = EntityUtils.toString(e);
    			//data = "["+data+"]";
    			JSONArray jarray = new JSONArray(data);
    			JSONObject jdata = null;
                
    			int id;
    			String nombre;
    			String url_icono;
    			ArrayList arreglo;
    			//Se recorre el vector json con los datos
                for(int i = 0; i < jarray.length(); i++){
                	jdata = jarray.getJSONObject(i);
                	id = jdata.getInt("id");
                	nombre = jdata.getString("nombre");
                	url_icono = jdata.getString("url_icono");	
                }
                
    			//Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    			/*Global.getInstance().setUserId(datos.getInt("id"));
    			login();*/
    		}else {
    			HttpEntity e = response.getEntity();
    			String data = EntityUtils.toString(e);
				error("eee");
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
		
		//return null;
    }
    
    public void error(String error){
		Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrar.vibrate(800);
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}
}
