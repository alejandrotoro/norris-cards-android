package com.norris.cards;

import com.norris.cards.Global;

public class Global {
	static private Global instance = null;
	private String production = "http://norris-cards.herokuapp.com";
	private String development = "http://192.168.0.13:3000";
	private String userEmail;
	private String usuario;
	private Integer userId;
	private Integer barajaId;
	private String barajaNombre;
	private Integer partidaId;
	
	private Global(){
		
	}
	
	static public Global getInstance(){
		if(instance == null){
			instance = new Global();
		}
		return instance;
	}

	public String getProduction() {
		return production;
	}

	public String getDevelopment() {
		return development;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String email) {
		this.userEmail = email;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String user) {
		this.usuario = user;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer id) {
		this.userId = id;
	}

	public Integer getBarajaId() {
		return barajaId;
	}

	public void setBarajaId(Integer barajaId) {
		this.barajaId = barajaId;
	}

	public String getBarajaNombre() {
		return barajaNombre;
	}

	public void setBarajaNombre(String barajaNombre) {
		this.barajaNombre = barajaNombre;
	}
	
	public Integer getPartidaId() {
		return partidaId;
	}

	public void setPartidaId(Integer partidaId) {
		this.partidaId = partidaId;
	}
}