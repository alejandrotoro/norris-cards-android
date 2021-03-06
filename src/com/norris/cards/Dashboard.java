package com.norris.cards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends Activity {
	private Button btnCrearPartida;
	private Button btnBuscarPartidas;
	private Button btnVerBarajas;
	
	public void goBack(View view){
		Intent intent = new Intent(Dashboard.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void settings(View view){
		Intent intent = new Intent(Dashboard.this, UserSettings.class);
		startActivity(intent);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        
        //seteo ID de baraja para simular pues esto debe venir al seleccionar baraja
        Global.getInstance().setBarajaId(1);
        Global.getInstance().setBarajaNombre("Nombre seteado de baraja");
        
        btnCrearPartida = (Button)this.findViewById(R.id.btnCrearPartida);
        btnBuscarPartidas = (Button)this.findViewById(R.id.btnBuscarPartidas);
        btnVerBarajas = (Button)this.findViewById(R.id.btnVerBarajas);
        
        //accion para boton Crear Partida
        btnCrearPartida.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Dashboard.this, CrearPartidaActivity.class);
				startActivity(intent);
			}
		});
        
        //accion para boton Buscar partidas activas
        btnBuscarPartidas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Dashboard.this, BuscarPartidasActivity.class);
				startActivity(intent);
			}
		});
		   
		//Ver barajas
        btnVerBarajas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Dashboard.this, VerBarajaActivity.class);
				startActivity(intent);
			}
		});
    }
}