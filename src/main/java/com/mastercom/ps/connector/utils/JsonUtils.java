package com.mastercom.ps.connector.utils;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;

/**
 * Classe di Utilità con metodi per la gestione delle request in formato
 * <code>xml/json</code>
 * 
 * <p>
 * 
 * @author SabatiniJa
 * @since 25/09/2018
 */
public final class JsonUtils {

	private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Costante valorizzata in fase di creazione oggetto, rappresenta la stringa
	 * json in input
	 */
	private final String json;
	private JSONObject xmlJSONObj;

	/**
	 * Crea un JsonUtils e trasforma l'xml in input in una stringa json, tramite un
	 * oggetto {@link JSONObject}. Il json viene formattato in base agli argomenti
	 * passati metodo {@link XML#toJSONObject(String, boolean)}
	 * 
	 * @param xml
	 *            - stringa che rappresenta l'xml in input
	 * @param keepStrings
	 *            - Se true, allora i valori dei tag non saranno forzati in valori
	 *            booleani o numerici e saranno invece lasciati come stringhe
	 */
	public JsonUtils(final String xml, final boolean keepStrings) {

		xmlJSONObj = XML.toJSONObject(xml, keepStrings);
		this.json = xmlJSONObj.toString();
		log.debug("json intermedio: " + this.getJson());

	}

	/**
	 * getter del json
	 * 
	 * @return json valorizzato e formattato tramite costruttore di classe
	 */
	public String getJson() {
		return json;
	}

	/**
	 * Genera una stringa json formattata in base ai
	 * valori passati come argomento.
	 * 
	 * @param xml
	 * @param PRETTY_PRINT_INDENT_FACTOR
	 *            - argomento passato al metodo {@link JSONObject#toString(int)}
	 * @return json formattato.
	 */
	public String getJsonFromXml(final String xml, final boolean keepStrings, final int PRETTY_PRINT_INDENT_FACTOR) {
		JSONObject xmlJSONObj = XML.toJSONObject(xml, keepStrings);
		return xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
	}

	/**
	 * Genera la risorsa rest in formato json
	 * 
	 * @param json
	 * @param headName
	 * @return la risorsa rest
	 */
	public String createRestJson(final String json, final String headName) {
		final int headIndexOf = (json.indexOf("{\"" +headName + "\":"));
		final int LEN = ("{\"" +headName + "\":").length();
		log.debug("headName: " + headName +" LEN: " + LEN);
		String tmp = json.substring(LEN, json.lastIndexOf("}"));
		log.info("json: " + tmp);
		return tmp;
	}
}
