package com.norris.cards.modelo;

/**
 * Clase que simboliza una carta de la baraja, sirve como esqueleto de datos que se presentan en el tablero de juego
 * 
 * @author Alfonso Baquero Echeverry, Administrador de Sistemas Informáticos
 *
 */
public class Carta {
	/** Identificador de la carta en la BD*/
	private String carta_id = "";
	/**	Número y letra de la carta*/
	private String num_letra = "";
	/**	Colores de la bandera*/
	private String colores = "";
	/**	Letras de nacionalidad*/
	private String letras = "";
	/**	Característica 1*/
	private String cr1 = "";
	/**	Característica 2*/
	private String cr2 = "";
	/**	Característica 3*/
	private String cr3 = "";
	/**	Característica 4*/
	private String cr4 = "";
	/**	Característica 5*/
	private String cr5 = "";
	/**
	 * @param carta_id
	 * @param num_letra
	 * @param colores
	 * @param letras
	 * @param cr1
	 * @param cr2
	 * @param cr3
	 * @param cr4
	 * @param cr5
	 */
	public Carta(String carta_id, String num_letra, String colores,
			String letras, String cr1, String cr2, String cr3, String cr4,
			String cr5) {
		super();
		this.carta_id = carta_id;
		this.num_letra = num_letra;
		this.colores = colores;
		this.letras = letras;
		this.cr1 = cr1;
		this.cr2 = cr2;
		this.cr3 = cr3;
		this.cr4 = cr4;
		this.cr5 = cr5;
	}
	/**
	 * @return the carta_id
	 */
	public String getCarta_id() {
		return carta_id;
	}
	/**
	 * @param carta_id the carta_id to set
	 */
	public void setCarta_id(String carta_id) {
		this.carta_id = carta_id;
	}
	/**
	 * @return the num_letra
	 */
	public String getNum_letra() {
		return num_letra;
	}
	/**
	 * @param num_letra the num_letra to set
	 */
	public void setNum_letra(String num_letra) {
		this.num_letra = num_letra;
	}
	/**
	 * @return the colores
	 */
	public String getColores() {
		return colores;
	}
	/**
	 * @param colores the colores to set
	 */
	public void setColores(String colores) {
		this.colores = colores;
	}
	/**
	 * @return the letras
	 */
	public String getLetras() {
		return letras;
	}
	/**
	 * @param letras the letras to set
	 */
	public void setLetras(String letras) {
		this.letras = letras;
	}
	/**
	 * @return the cr1
	 */
	public String getCr1() {
		return cr1;
	}
	/**
	 * @param cr1 the cr1 to set
	 */
	public void setCr1(String cr1) {
		this.cr1 = cr1;
	}
	/**
	 * @return the cr2
	 */
	public String getCr2() {
		return cr2;
	}
	/**
	 * @param cr2 the cr2 to set
	 */
	public void setCr2(String cr2) {
		this.cr2 = cr2;
	}
	/**
	 * @return the cr3
	 */
	public String getCr3() {
		return cr3;
	}
	/**
	 * @param cr3 the cr3 to set
	 */
	public void setCr3(String cr3) {
		this.cr3 = cr3;
	}
	/**
	 * @return the cr4
	 */
	public String getCr4() {
		return cr4;
	}
	/**
	 * @param cr4 the cr4 to set
	 */
	public void setCr4(String cr4) {
		this.cr4 = cr4;
	}
	/**
	 * @return the cr5
	 */
	public String getCr5() {
		return cr5;
	}
	/**
	 * @param cr5 the cr5 to set
	 */
	public void setCr5(String cr5) {
		this.cr5 = cr5;
	}
}
