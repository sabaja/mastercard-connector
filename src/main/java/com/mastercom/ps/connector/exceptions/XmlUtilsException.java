package com.mastercom.ps.connector.exceptions;


/**
 * Classe per la gestione delle eccezioni relative alle procedure di
 * estrapolazione xml della classe {@link XmlUtils}
 * 
 * 
 * @author SabatiniJa
 * @since 25/07/2018
 */
public class XmlUtilsException extends Exception {

	private static final long serialVersionUID = 6228961925500567214L;

	public XmlUtilsException() {
	}

	public XmlUtilsException(String message) {
		super(message);
	}

	public XmlUtilsException(Throwable cause) {
		super(cause);
	}

	public XmlUtilsException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmlUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
