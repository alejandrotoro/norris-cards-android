package com.norris.cards;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VerBarajaActivity extends Activity {
	
	public LinkedList<String[][]> lstbarajas;//Se define la lista de barajas
	public String url = Global.getInstance().getProduction();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_baraja);
        
		ListView l = (ListView) findViewById(R.id.ListView01);
		l.setAdapter(new miAdapter(this));
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
 
		private static final String[][] data = {{"Carros","1","http://colombia.golgolgol.net/images_repository/0/9404_escudo.png_fd919ee6196ded3f4983b4ee2fa91bb4.png"}, 
												   {"Chicas exÃ³ticas","2","http://i.imgur.com/LHJiA.jpg"}, 
												   {"Choferes","3","http://i.imgur.com/VsVMQ.jpg"}, 
												   {"Futbolistas","4","http://colombia.golgolgol.net/images_repository/0/9404_escudo.png_fd919ee6196ded3f4983b4ee2fa91bb4.png"}, 
												   {"Presentadoras","5","http://i.imgur.com/LHJiA.jpg"}, 
												   {"Sexo","5","http://i.imgur.com/VsVMQ.jpg"}, 
												   {"Amor","5","http://i.imgur.com/LHJiA.jpg"}, 
												   {"TecnologÃ­a","5","http://i.imgur.com/VsVMQ.jpg"}};
 
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
 
			text.setText(data[position][0]);
			
			//Se muestra la imagen de la bajara que se trae desde el servidor
	        imageManager.fetchDrawableOnThread(data[position][2], imgBaraja);
 
	        imgNext.setId(Integer.parseInt(data[position][1]));
	        imgNext.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(actividad, "eee id: "+v.getId(),Toast.LENGTH_SHORT).show();
				}
			});
	        
			return convertView;
		}
 
 
		public int getCount() {
 
			return data.length;
 
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
     * Método para obtener las barajas desde el servidor y meterlas en un array
     * 
     */
    private void obtenerBarajas(){
        List valores = new ArrayList();
        InputStream is = null;
        /*
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url+"/barajas.json");
            
            //recibe la respuesta del servidor
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            BufferedReader reader =  new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            
            String line = "0";
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            
            is.close();
            result = sb.toString();
            
            int prestamoId;
            String clienteNombre;
            String clienteTelefono;
            String fecha;
            int cuotas;
            int diaCobro;
            String fechaCobro;
            Double monto;
            Double saldo;
            Double valorCuota;

            
            JSONArray jArray = new JSONArray(result);
            JSONObject json_data = null;
            
            //Se recorre el vector json con los datos
            for(int i = 0; i < jArray.length(); i++){
             json_data = jArray.getJSONObject(i);
                prestamoId = json_data.getInt("idprestamo");
                clienteNombre = json_data.getString("nombre");
                clienteTelefono = json_data.getString("telefono");
                fecha = json_data.getString("fecha");
                cuotas = json_data.getInt("cuotas");
                diaCobro = json_data.getInt("diacobro");
                fechaCobro = json_data.getString("fechacobro");
                monto = json_data.getDouble("monto");
                saldo = json_data.getDouble("saldo");
                valorCuota = json_data.getDouble("valorcuota");
                //Se pobla el vector de prestamos
                lstprestamos.add(new Prestamo(prestamoId, clienteNombre,clienteTelefono,fecha,cuotas,diaCobro,fechaCobro,monto,saldo,valorCuota));
            }
            
            //Creamos el adaptador
            ArrayAdapter<Prestamo> spinner_adaptere = new ArrayAdapter<Prestamo>(AListaPrestamos.this, android.R.layout.simple_spinner_item, lstprestamos);
            //Añadimos el layout para el menú y se lo damos al spinner
            spinner_adaptere.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPPrestamos.setAdapter(spinner_adaptere);
            //spPPrestamos.setAdapter(spinner_adaptere);
      
        }catch(Exception e){
            Log.e("**ERROR", "Error conexion " + e.toString());
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }*/
    }
}
