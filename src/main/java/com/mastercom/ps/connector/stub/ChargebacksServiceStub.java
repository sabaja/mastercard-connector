package com.mastercom.ps.connector.stub;

import com.mastercard.api.mastercom.Chargebacks; 

/**
 * Questa enumerazione è la rappresentazione dei metodi del servizio
 * {@link Chargebacks} di Mastercard
 * 
 * @author SabatiniJa
 *
 */
public enum ChargebacksServiceStub {
	Create,
	CreateReversal,
	GetPossibleValueListsForCreate,
	RetrieveDocumentation,
	Update,
	AcknowledgeReceivedChargebacks,
	ChargebacksStatus;
};
