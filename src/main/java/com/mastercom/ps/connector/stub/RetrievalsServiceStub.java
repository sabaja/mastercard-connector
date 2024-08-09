package com.mastercom.ps.connector.stub;

import com.mastercard.api.mastercom.Retrievals; 


/**
 * Questa enumerazione è la rappresentazione dei metodi del servizio
 * {@link Retrievals} di Mastercard
 * 
 * @author SabatiniJa
 *
 */
public enum RetrievalsServiceStub {
	Create,
	AcquirerFulfillARequest,
	IssuerRespondToFulfillment,
	GetPossibleValueListsForCreate,
	GetDocumentation,
	RetrievalFullfilmentStatus;
};