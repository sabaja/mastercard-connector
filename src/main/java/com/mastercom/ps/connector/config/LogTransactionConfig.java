package com.mastercom.ps.connector.config;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import java.util.UUID;

/**
 * Classe di configurazione per creare un Uid a livello di log univoco per ogni
 * request
 * 
 * @author SabatiniJa
 *
 */
public class LogTransactionConfig {
	protected static final Logger LOGGER = Logger.getLogger(LogTransactionConfig.class);

	/**
	 * Metodo per l'inizializzazione Uid
	 */
	public void requestInitialized() {
		LOGGER.debug("************ REQUEST INIZIALIZZATA ************");
		MDC.put("RequestId", UUID.randomUUID());
	}

	/**
	 * Metodo per la distruzione Uid
	 */
	public void requestDestroyed() {
		LOGGER.debug("************ REQUEST DISTRUTTA ************");
		MDC.clear();
	}
}