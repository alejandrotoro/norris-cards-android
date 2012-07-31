package com.norris.cards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VerBarajaActivity extends Activity {

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
												   {"Chicas exóticas","2","http://i.imgur.com/LHJiA.jpg"}, 
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
}
