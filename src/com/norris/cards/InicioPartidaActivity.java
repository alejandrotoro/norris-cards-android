package com.norris.cards;

import com.norris.cards.modelo.Funciones;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class InicioPartidaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_partida);
    }
    
    /**
     * invoca al tablero de juego, la idea es que antes de invocar al tablero de juego compruebe que se inicie
     * un juego creado por el usario activo y que la cantidad de jugadores sea correcta, si no se cumplen estas
     * condiciones, se muestra advertencia y se permanece en la pantalla sin cargar tablero.
     * */
    public void iniciar(View view){
    	int user_id = Global.getInstance().getUserId();
    	int prt_id = Global.getInstance().getPartidaId();
    	String msg = Funciones.prtStartCheck(user_id, prt_id);
    	if(msg.equals("")){ // si el mensaje esta vacio, la partida es válida para iniciar
    		// se reparten las cartas, se hace un máximo de 5 intentos para el sorteo de cartas
    		int tries = 1;
			Toast.makeText(this, "Repartiendo cartas entre los jugadores.\nIntento 1 de 5", 10000).show();
    		while(!Funciones.sortCards(prt_id) && tries <= 5){
    			tries++;
    			Toast.makeText(this, "Repartiendo cartas entre los jugadores.\nIntento "+tries+" de 5", 10000).show();
    		}
    		
    		// si las cartas se reparten exitosamente
    		if(tries <= 5){
    			Toast.makeText(this, "Sorteo de cartas exitoso, cargando tablero de juego", Toast.LENGTH_LONG).show();
    		}else{
    			Toast.makeText(this, "Se ha superado el tope de intentos de sorteo de cartas", Toast.LENGTH_LONG).show();
    		}
    	}else{
    		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    	}
    }
}
