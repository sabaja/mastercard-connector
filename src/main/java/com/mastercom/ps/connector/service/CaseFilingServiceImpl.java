package com.mastercom.ps.connector.service;

import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.mastercom.CaseFiling;

/**
 * Classe Service per le chiamate Rest vs MastrerCard
 * 
 * @author SabatiniJa
 *
 */
public class CaseFilingServiceImpl implements CaseFilingService<CaseFiling, RequestMap> {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8480424631637684504L;
	// private CaseFiling caseFiling;
	//
	//
	// public CaseFilingServiceImpl(CaseFiling caseFiling) {
	// this.caseFiling = caseFiling;
	// }

	@Override
	public CaseFiling create(RequestMap map) throws ApiException {
		return CaseFiling.create(map);
	}

	@Override
	public CaseFiling retrieveDocumentation(RequestMap map) throws ApiException {
		return CaseFiling.retrieveDocumentation(map);
	}

	@Override
	public CaseFiling update(RequestMap map) throws ApiException {
		return new CaseFiling(map).update();
	}

	@Override
	public CaseFiling caseFilingStatus(RequestMap map) throws ApiException {
		return new CaseFiling(map).caseFilingStatus();
	}

}
