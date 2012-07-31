package com.norris.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterListaPartidas extends BaseAdapter {
	private LayoutInflater myInflater;
	private String[][] datos;
	private Context c;
	
	public AdapterListaPartidas(Context c, String[][] datos){
		this.myInflater = LayoutInflater.from(c);
		this.datos = datos;
		this.c = c;
	}

	public int getCount() {
		return this.datos.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = myInflater.inflate(R.layout.lista_partidas, null);
		}
		
		TextView tvBaraja = (TextView)convertView.findViewById(R.id.tvBaraja);
		TextView tvCreador = (TextView)convertView.findViewById(R.id.tvCreador);
		ImageView btnIr = (ImageView)convertView.findViewById(R.drawable.back_btn);
		btnIr.setId(1);
		
		btnIr.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(c, ""+v.getId(), Toast.LENGTH_LONG).show();
			}
		});
		
		tvBaraja.setText(datos[position][0]);
		tvCreador.setText(datos[position][1]);
		
		return convertView;
	}

}
