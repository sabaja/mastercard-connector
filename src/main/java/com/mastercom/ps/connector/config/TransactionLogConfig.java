package com.mastercom.ps.connector.config;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import java.util.UUID;

/**
 * Classe di configurazione Log</br>
 * <pre>
 * Crea una variabile con il servizio invocato per ogni request 
 * Crea un Uid univoco per ogni request
 * </pre>
 * 
 * @author SabatiniJa
 *
 */
public class TransactionLogConfig {
	protected static final Logger LOGGER = Logger.getLogger(TransactionLogConfig.class);
	/**
	 * 
	 */
	public TransactionLogConfig(String serviceName) {
		super();
		this.requestInitialized(serviceName);
	}

	/**
	 * Metodo per l'inizializzazione Uid
	 */
	private void requestInitialized(String serviceName) {
		MDC.put("RequestId", UUID.randomUUID());
		if (null == serviceName) {
			serviceName = "";
		}
		MDC.put("ServiceName", serviceName);
		LOGGER.debug("");
	}

	/**
	 * Metodo per la distruzione Uid
	 */
	public void requestDestroyed() {
		LOGGER.debug("************ REQUEST DISTRUTTA ************");
		MDC.clear();
	}

}