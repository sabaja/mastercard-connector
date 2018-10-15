package com.mastercom.ps.connector.response.domain.casefiling;

import com.mastercard.api.mastercom.CaseFiling;

/**
 * Interfaccia per la gestione delle response di tipo
 * {@link CaseFiling}
 * 
 * @author SabatiniJa
 *
 */
public interface CaseFilingResponseHandler<T extends CaseFiling> {

	/**
	 * Metodo che si occupa della gestione della response del metodo 
	 * {@link CaseFiling#create(com.mastercard.api.core.model.RequestMap)}
	 * 
	 * @return response in formato xml
	 * @throws Exception 
	 */
	public String getCreateResponse(final T resource, String method) throws Exception;

	/**
	 * Metodo che si occupa della gestione della response del metodo 
	 * {@link CaseFiling#retrieveDocumentation(com.mastercard.api.core.model.RequestMap)}
	 * 
	 * @return response in formato xml
	 * @throws Exception 
	 */
	public String getRetrieveDocumentationResponse(final T resource, String method) throws Exception;

	/**
	 * Metodo che si occupa della gestione della response del metodo 
	 * {@link CaseFiling#update()}
	 * 
	 * @return response in formato xml
	 */
	public String getUpdateResponse(final T resource, String method);

	/**
	 * Metodo che si occupa della gestione della response del metodo 
	 * {@link CaseFiling#caseFilingStatus()}
	 * 
	 * @return response in formato xml
	 */
	public String getCaseFilingStatusResponse(final T resource, String method);
}
