package com.mastercom.ps.connector.service;

import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.mastercom.CaseFiling;

public interface CaseFilingService <T extends CaseFiling, R extends RequestMap>{

	public T create(R map) throws ApiException;
	
	public T retrieveDocumentation(R map) throws ApiException;
	
	public T update(R map) throws ApiException; 
	
	public T caseFilingStatus(R map) throws ApiException;
}
