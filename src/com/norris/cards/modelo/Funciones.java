package com.norris.cards.modelo;

import java.util.Vector;
import com.norris.cards.conexion.ConexionBD;

/**
 * @author Alfonso Baquero Echeverry, Administrador de Sistemas Informáticos
 *
 */
public class Funciones {
	/**
	 * Función para verificar la autenticidad del inicio de una partida
	 * @param user_id Id de usario que inicia la partida
	 * @param prt_id Id de la partida que esta iniciandose
	 * @return Mensaje de respuesta, cadena vacía en caso de inicio correcto
	 * */
	public static String prtStartCheck(int user_id, int prt_id){
		String res = ""; // mensaje de salida
		/* se establece si el usario activo es el creador de la partida y que la partida no este en curso ya */
		String cons = "" +
			"select prt.cantidad_jugadores " +
			"from partidas prt " +
			"where prt.creador_id = "+user_id+" " +
			"and prt.id = "+prt_id+" " +
			"and prt.estado = false";
		
		Vector<String[]> bd_res = ConexionBD.doSelectJSON(cons);
		if(bd_res.size() <= 1){
			res = "Lo sentimos, usted no puede iniciar una partida creada por otro jugador.";
		}else{
			/* si la partida existe y fue creada por el usuario activo, se verifica que la cantidad de jugadores inscritos complete el cupo */
			int cont_jug = 3;
			try{
				cont_jug = Integer.parseInt(bd_res.get(1)[0]);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
			
			bd_res = getPlayers(prt_id);
			
			if(bd_res.size() < cont_jug){
				res = "Aún no se completa el cupo de jugadores para iniciar la partida.";
			}
		}
		return res;
	}
	
	/**
	 * Función para obtener el listado de los jugadores inscritos en una partida
	 * @param prt_id Id de la partida seleccionada
	 * @return Listado de los participantes
	 * */
	public static Vector<String[]> getPlayers(int prt_id){
		Vector<String[]> res = new Vector<String[]>();
		String cons = "" +
			"select uprt.usuario_id " +
			"from partidas prt, usuario_partidas uprt " +
			"where prt.id = uprt.partida_id " +
			"and prt.id = "+prt_id+" " +
			"and prt.estado = false";
		
		res = ConexionBD.doSelectJSON(cons);
		if(res.size() > 0){
			res.removeElementAt(0); // elimino la fila de encabezado
		}
		
		return res;
	}
	
	/**
	 * Función para repartir aleatoriamente las cartas de una baraja entre los jugadores presentes en una partida, 
	 * las cartas que se le asignan a cada jugador se referencian en la BD en la tabla "usuario_carta"
	 * @param prt_id Id de la partida
	 * @return True en caso de éxito, false si se presenta algún problema
	 * */
	public static boolean sortCards(int prt_id){
		boolean res = true;
		
		// obtengo un listado de los usuarios de la partida
		Vector<String[]> users = getPlayers(prt_id);
	
		// consulto los id de las cartas en la baraja
		String cons = "" +
			"select c.id " +
			"from partidas prt, carta c " +
			"where prt.baraja_id = c.id_baraja " +
			"and prt.id = "+prt_id+" " +
			"order by c.id";
		
		Vector<String[]> bd_res = ConexionBD.doSelectJSON(cons);
		if(bd_res.size() > 1){
			bd_res.removeElementAt(0); // se elimina la fila de encabezado
			int cant_crd = bd_res.size(); // se almacena la cantidad de cartas en la baraja
			
			/* una vez se tiene el listado de las cartas y los usuarios, estas se reparten aleatoriamente empleando el indice en el listado de las mismas, 
			 * cada carta que se asigna, es retirada de la lista y el ciclo continua hasta que la lista queda vacía*/
			
			int user_idx = 0; // indice para el listado de jugadores
			int orden = 1; // indicador de orden de la carta en la mano del jugador
			while(bd_res.size() > 0){
				double rdm = Math.random();
				int ctr_idx = (int)((bd_res.size()+1)*rdm);
				if(ctr_idx >= bd_res.size())
					ctr_idx = bd_res.size()-1;
				
				cons = "" +
					"insert into usuario_carta " +
					"(partida_id, usuario_id, carta_id, orden)" +
					"values(" +
						prt_id+", " +
						users.get(user_idx)[0]+", " +
						bd_res.get(ctr_idx)[0]+", " +
						orden+
					")";
				
				if(ConexionBD.doUpdateJSON(cons) > 0){
					// si la carta se asigna correctamente, se retira de la lista y se pasa al siguiente jugador
					bd_res.removeElementAt(ctr_idx);
					user_idx++;
					if(user_idx == users.size()){ // se vuelve al primer jugador y se aumenta el orden al terminar el recorrido
						user_idx = 0;
						orden++;
					}
				}else{
					res = false;
					break;
				}
			}
			
			// si para este punto no se han presentado problemas en la asignación de cartas, se comprueba que no se entregara mas cartas de la cuenta
			cons = "" +
				"select count(*) " +
				"from usuario_carta " +
				"where partida_id = "+prt_id;
			
			bd_res = ConexionBD.doSelectJSON(cons);
			if(bd_res.size() > 1){
				if(cant_crd < Integer.parseInt(bd_res.get(1)[0])){
					// si se reparten mas cartas, se limpia la asignación fallida
					cons = "" +
						"delete from usuario_carta where partida_id = "+prt_id;
					
					ConexionBD.doUpdateJSON(cons);
					
					res = false;
				}
			}
		}else{
			res = false;
		}
		return res;
	}
	
	/**
	 * Procedimiento para cargar la información de una carta en el objeto carta de la sesión
	 * @param prt_id ID de la partida
	 * @param user_id ID del usuario
	 * */
	public static void loadCard(String prt_id, String user_id){
		/*String cons = "" +
			"select ";*/
	}
}
