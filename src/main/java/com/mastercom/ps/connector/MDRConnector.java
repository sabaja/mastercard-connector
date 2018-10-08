package com.mastercom.ps.connector;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.InternetHeaders;

import org.apache.log4j.Logger;

import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.mastercom.CaseFiling;
import com.mastercom.ps.connector.config.TransactionLogConfig;
import com.mastercom.ps.connector.config.ServiceConfiguration;
import com.mastercom.ps.connector.examples.tests.CaseFillingStatusReq;
import com.mastercom.ps.connector.exceptions.XmlUtilsException;
import com.mastercom.ps.connector.response.domain.casefiling.CaseFilingHandler;
import com.mastercom.ps.connector.response.domain.casefiling.CaseFilingHandlerImpl;
import com.mastercom.ps.connector.service.CaseFilingService;
import com.mastercom.ps.connector.service.CaseFilingServiceImpl;
import com.mastercom.ps.connector.utils.JsonUtils;
import com.mastercom.ps.connector.utils.XmlUtils;
import com.peoplesoft.pt.integrationgateway.common.ConnectorDataCollection;
import com.peoplesoft.pt.integrationgateway.common.DuplicateMessageException;
import com.peoplesoft.pt.integrationgateway.common.ExternalApplicationException;
import com.peoplesoft.pt.integrationgateway.common.ExternalSystemContactException;
import com.peoplesoft.pt.integrationgateway.common.GeneralFrameworkException;
import com.peoplesoft.pt.integrationgateway.common.InvalidMessageException;
import com.peoplesoft.pt.integrationgateway.common.MessageMarshallingException;
import com.peoplesoft.pt.integrationgateway.common.MessageUnmarshallingException;
import com.peoplesoft.pt.integrationgateway.framework.ConnectorInfo;
import com.peoplesoft.pt.integrationgateway.framework.IBRequest;
import com.peoplesoft.pt.integrationgateway.framework.IBResponse;
import com.peoplesoft.pt.integrationgateway.framework.InternalIBResponse;
import com.peoplesoft.pt.integrationgateway.targetconnector.TargetConnector;
import com.thoughtworks.xstream.XStream;

/**
 * Classe di connessione tra l'Integration Broker di Peoplesoft e l'API di
 * MasterCard.</br>
 * Il connettore è subordinato all'interfaccia {@link TargetConnection} che, per
 * ogni request da inviare a Mastercard, viene invocato il metodo
 * {@link MDRConnector#send(IBRequest)} da PeopleSoft.<br>
 * Tale invocazione mette in moto un processo diviso in quattro Stadi
 * <u>consecutivi</u>:<br>
 * <ol type="1">
 * <li>Ricezione request in formato xml</li>
 * <li>Elaborazione dati:
 * <ul>
 * <li>Conversione da xml in json</li>
 * <li>Implementazione oggetto Rest con i dati presenti nel json</li>
 * </ul>
 * </li>
 * <li>Apertura connessione tramite {@link SSLConnectionSocketFactory} verso
 * Mastercard:
 * <ul>
 * <li>Invio request</li>
 * <li>Ricezione response</li>
 * </ul>
 * <li>Elaborazione dati response, con conversione da oggetto Rest (URI di tipo
 * ) a xml</li>
 * <li>Invio a Peoplesoft della response</li>
 * </ol>
 * 
 * </br>
 * Il connettore è <em>thread-safe</em> </br>
 * In caso di eccezione, l'errore viene incapsulato in un tag
 * <code>faultcode</code> di un xml e spedito a PeopleSoft.
 * 
 */
public class MDRConnector implements TargetConnector {

	private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private ConnectorInfo connInfo;
	private ServiceConfiguration serviceConfiguration;
	private XStream xstream;

	public void init(ConnectorInfo connInfo) {
		this.serviceConfiguration = new ServiceConfiguration(connInfo);
		this.xstream = new XStream();
		XStream.setupDefaultSecurity(xstream);

	}

	public ConnectorDataCollection introspectConnector() {
		return null;
	}

	@Override
	public IBResponse ping(IBRequest request) throws GeneralFrameworkException, DuplicateMessageException,
			InvalidMessageException, ExternalSystemContactException, ExternalApplicationException,
			MessageMarshallingException, MessageUnmarshallingException {
		// TODO Auto-generated method stub
		return send(request);
	}

	@Override
	public IBResponse send(IBRequest request) throws GeneralFrameworkException, DuplicateMessageException,
			InvalidMessageException, ExternalSystemContactException, ExternalApplicationException,
			MessageMarshallingException, MessageUnmarshallingException {
		log.info("SEND | Inzio richiesta");
		String responseString = null;
		try {
			this.setConnInfo(request.getConnectorInfo());
			this.init(this.getConnInfo());

			final String xmlRequest = request.getContentSectionAt(0);
			log.info("Request from PS: " + xmlRequest);

		} catch (Exception e) {
			responseString = "<?xml version=\"1.0\"?>"
					+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<SOAP-ENV:Body>" + "<SOAP-ENV:Fault>" + "<faultcode>" + 500 + "</faultcode>" + "<faultstring>"
					+ e.getClass().getSimpleName() + "</faultstring>" + "<detail><Message>" + e.getMessage()
					+ "</Message></detail>" + "</SOAP-ENV:Fault>" + "</SOAP-ENV:Body>" + "</SOAP-ENV:Envelope>";
			// https://cloud.google.com/storage/docs/json_api/v1/status-codes
			log.error("Exception: " + responseString);
		}
		final InternalIBResponse response = new InternalIBResponse();
		final InternetHeaders internetHeaders = new InternetHeaders();

		try {
			response.addContentSection(internetHeaders, responseString);
		} catch (final Exception xe) {
			log.error("Exception: " + xe.getMessage() + " -> " + xe.toString() + " STACKTRACE " + xe.getStackTrace());
		}

		return null;
	}

	private ConnectorInfo getConnInfo() {
		return connInfo;
	}

	private void setConnInfo(ConnectorInfo connInfo) {
		this.connInfo = connInfo;
	}

	private ServiceConfiguration getServiceConfiguration() {
		return serviceConfiguration;
	}

	private void setServiceConfiguration(ServiceConfiguration serviceConfiguration) {
		this.serviceConfiguration = serviceConfiguration;
	}

	public static void main(String[] args) throws Exception {
		final boolean CASE = true;
		MDRConnector connector = new MDRConnector();

		if (!CASE) {
			connector.test();
		} else {
			// TODO
			// Configurazione da togliere
			String file = "C:\\Users\\sabatinija\\Desktop\\Devspace\\PeopleSoft\\Mastercards\\XML\\Request\\CaseFiling.retrieveDocumentation.xml";

			// Codice comune pre try
			String xml = "", xmlObjectRequest = "", jsonObjectRequest = "", serviceName = "";
			XmlUtils xmlUtils = null;
			JsonUtils jsonUtils = null;
			RequestMap requestMap = null;
			CaseFiling resource = null;
			XStream xstream = new XStream();
			XStream.setupDefaultSecurity(xstream);
			TransactionLogConfig transactionLogConfig = null;
			try {
				// TODO
				// Da togliere request gia presente
				xml = new String(Files.readAllBytes(Paths.get(file)));

				xmlUtils = new XmlUtils(xml);
				serviceName = xmlUtils.getMethod();
				System.out.println(serviceName);
				transactionLogConfig = new TransactionLogConfig(serviceName);
				xmlObjectRequest = xmlUtils.createRestObjectRequest();

				// true - i tag non vengono forzati a numerici/booleani ma rimango stringhe
				jsonUtils = new JsonUtils(xmlObjectRequest, true);

				jsonObjectRequest = jsonUtils.getJson();
				jsonObjectRequest = jsonUtils.createRestJson(jsonObjectRequest, xmlUtils.getHeadName());
				System.out.println("rest: " + jsonObjectRequest);
				connector.setServiceConfiguration(new ServiceConfiguration());
				requestMap = new RequestMap(jsonObjectRequest);

				// TODO
				// Inserire classe SWITCH
				CaseFilingService<CaseFiling, RequestMap> service = new CaseFilingServiceImpl();

				resource = service.retrieveDocumentation(requestMap);
				CaseFilingHandler<CaseFiling> caseFilingResponse = new CaseFilingHandlerImpl();
				String response = caseFilingResponse.getRetrieveDocumentationResponse(resource,
						"CaseFiling.retrieveDocumentation");
				// FINE

				System.out.println("response: " + response);
			} catch (Exception e) {
				String responseString = "<?xml version=\"1.0\"?>"
						+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<SOAP-ENV:Body>" + "<SOAP-ENV:Fault>" + "<faultcode>" + 500 + "</faultcode>"
						+ "<faultstring>" + e.getClass().getSimpleName() + "</faultstring>" + "<detail><Message>"
						+ e.getMessage() + "</Message></detail>" + "</SOAP-ENV:Fault>" + "</SOAP-ENV:Body>"
						+ "</SOAP-ENV:Envelope>";
				// https://cloud.google.com/storage/docs/json_api/v1/status-codes
				System.out.println(responseString);
			} finally {
				transactionLogConfig.requestDestroyed();
			}
		}
	}

	public void test() {
		CaseFillingStatusReq caseFilling = new CaseFillingStatusReq();
		caseFilling.send();
	}

	public static void out(Map<String, Object> map, String key) {
		System.out.println(key + "--->" + map.get(key));
	}

}
