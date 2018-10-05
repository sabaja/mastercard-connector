package com.mastercom.ps.connector.service;

import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercom.ps.connector.config.ServiceConfiguration;

@SuppressWarnings("unused")
public abstract class RetrievalsService<T extends com.mastercard.api.mastercom.Retrievals> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5491726962257905630L;
	private final ServiceConfiguration config;

	public RetrievalsService(ServiceConfiguration config) {
		this.config = config;
	}

	abstract public T create(RequestMap map) throws ApiException;

	abstract public T acquirerFulfillARequest(RequestMap map) throws ApiException;

	abstract public T issuerRespondToFulfillment(RequestMap map) throws ApiException;

	abstract public T getPossibleValueListsForCreate(RequestMap map) throws ApiException;

	abstract public T getDocumentation(RequestMap map) throws ApiException;

	abstract public T retrievalFullfilmentStatus(RequestMap map) throws ApiException;

}
