package com.norris.cards.conexion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

import com.norris.cards.Global;

/**
 * Clase para establecer la conexión con la base de datos
 * @author Alfonso Baquero Echeverry, Administrador de Sistemas Informáticos
 */
public class ConexionBD {
	/**
	 * Función para ejecutar una sentencia de tipo SELECT, retorna un arreglo con las filas seleccionadas, incluyendo el encabezado.
	 * Si el resultado de la sentencia es vacío, solo retorna el encabezado, si presenta error en la consulta, lo presenta en consola
	 * y retorna un arreglo con 0 filas, para evitar errores del tipo nullPointerExcoption
	 * @param sql Sentencia tipo SELECT a ejecutar
	 * @return Arreglo con el resultado de la consulta
	 * */
	public static Vector<String[]> doSelectJSON(String sql){
		Vector<String[]> res = new Vector<String[]>();
		String url = Global.getInstance().getProduction(); 	// se toma la ruta del servidor
		String auth = url+"/consulta.json"; 				// se especifica el servicio a invocar
		HttpClient httpclient = new DefaultHttpClient();	// se inicializa el cliente http
		HttpPost httppost = new HttpPost(auth);				// se configura un POST para enviar al servicio
		try {
			// se prepara el argumento a enviar al servicio con la sentencia SQL
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("sql", sql));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// se captura en la respuesta de la ejecución del llamado al servicio
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity e = response.getEntity();
			String data = EntityUtils.toString(e);
			JSONArray a;
			try {
				a = new JSONArray(data);
				for (int i=0; i<a.length(); i++) {
					String[] fila = a.getJSONObject(i).toString().replace("{", "").replace("}", "").replace("\"", "").split(",");
					
					if(i == 0){ // para la primera línea se saca el encabezado
						String[] enc = new String[fila.length];
						for(int j=0; j<fila.length; j++){
							enc[j] = fila[j].split(":")[0];
						}
						res.add(enc);
					}
					
					// se leen los datos de la respuesta y se agregan al vector de retorno
					for(int j=0; j<fila.length; j++){
						try{
							fila[j] = fila[j].split(":")[1];
						}catch(ArrayIndexOutOfBoundsException e1){
							fila[j] = "";
						}
					}
					res.add(fila);
				}
			} catch (JSONException e1) {
				Global.getInstance().setMessages("Error de Conexión");
			}
		} catch (UnsupportedEncodingException e) {
			Global.getInstance().setMessages("Error de codificación erronea");
		} catch (ClientProtocolException e) {
			Global.getInstance().setMessages("Error de protocolo de cliente");
		} catch (IOException e) {
			Global.getInstance().setMessages("Error de entrada/salida");
		}
		return res;
	}
	
	/**
	 * Función para la ejecución de sentencias del tipo UPDATE, DELETE e INSERT, retorna la cantidad de filas alteradas.
	 * Si se presenta un error durante la ejecución de la sentencia, lo presenta en consola y retorna 0
	 * @param sql Sentencia a ejecutar
	 * @return Cantidad de filas alteradas o ingresadas
	 * */
	public static int doUpdateJSON(String sql){
		String url = Global.getInstance().getProduction(); 	// se toma la ruta del servidor
		String auth = url+"/consulta.json"; 				// se especifica el servicio a invocar
		HttpClient httpclient = new DefaultHttpClient();	// se inicializa el cliente http
		HttpPost httppost = new HttpPost(auth);				// se configura un POST para enviar al servicio
		try {
			// se prepara el argumento a enviar al servicio con la sentencia SQL
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("sql", sql));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// se captura en la respuesta de la ejecución del llamado al servicio
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity e = response.getEntity();
			String data = EntityUtils.toString(e);
			System.out.println(data);
		} catch (UnsupportedEncodingException e) {
			Global.getInstance().setMessages("Error de codificación erronea");
		} catch (ClientProtocolException e) {
			Global.getInstance().setMessages("Error de protocolo de cliente");
		} catch (IOException e) {
			Global.getInstance().setMessages("Error de entrada/salida");
		}
		return 1;
	}
}
