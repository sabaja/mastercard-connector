package com.mastercom.ps.connector.service;

import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.mastercom.Retrievals;
import com.mastercom.ps.connector.config.ServiceConfiguration;

public class RetrievalsServiceImpl extends RetrievalsService<Retrievals>{

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3628085875399092063L;
	
	public RetrievalsServiceImpl(ServiceConfiguration config) {
		super(config);
	}

	@Override
	public Retrievals create(RequestMap map) throws ApiException {
		return Retrievals.create(map);
	}

	@Override
	public Retrievals acquirerFulfillARequest(RequestMap map) throws ApiException {
		return Retrievals.acquirerFulfillARequest(map);
	}

	@Override
	public Retrievals issuerRespondToFulfillment(RequestMap map) throws ApiException {
		return Retrievals.issuerRespondToFulfillment(map);
	}

	@Override
	public Retrievals getPossibleValueListsForCreate(RequestMap map) throws ApiException {
		return Retrievals.getPossibleValueListsForCreate(map);
	}

	@Override
	public Retrievals getDocumentation(RequestMap map) throws ApiException {
		return Retrievals.getDocumentation(map);
	}

	@Override
	public Retrievals retrievalFullfilmentStatus(RequestMap map) throws ApiException {
		Retrievals response = new Retrievals(map).retrievalFullfilmentStatus();
		return response;
	}


}
