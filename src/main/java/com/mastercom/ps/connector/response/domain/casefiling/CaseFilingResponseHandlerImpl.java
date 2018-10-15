package com.mastercom.ps.connector.response.domain.casefiling;

import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.mastercard.api.mastercom.CaseFiling;

/**
 * Classe che si occupa di fare il parsing delle response relative alle request
 * dellla classe {@link CaseFiling}
 * 
 * @author SabatiniJa
 *
 */
public class CaseFilingResponseHandlerImpl implements CaseFilingResponseHandler<CaseFiling> {

	private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public String getCreateResponse(CaseFiling resource, String method) throws Exception {
		return null;
	}

	@Override
	public String getRetrieveDocumentationResponse(CaseFiling resource, String method) throws Exception {
		ObjectFactory factory = new ObjectFactory();
		CaseFilingData caseFiling = factory.createCaseFilingResponseData();
		/*
		 * (response, "fileAttachment.filename"); //-->CS_536092.zip out(response,
		 * "fileAttachment.file");
		 */
		caseFiling.setMethod(method);
		FileAttachment fileAttachment = factory.createFileAttachment();
		fileAttachment.setFile((String) resource.get("fileAttachment.file"));
		fileAttachment.setFilename((String) resource.get("fileAttachment.filename"));
		caseFiling.setFileAttachment(fileAttachment);

		JAXBContext context = JAXBContext.newInstance(this.getThisPackage());
		JAXBElement<CaseFilingData> element = factory.createResponseData(caseFiling);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.FALSE);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		marshaller.marshal(element, out);
		String response = new String(out.toByteArray());
		return response;
	}

	@Override
	public String getUpdateResponse(CaseFiling resource, String method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCaseFilingStatusResponse(CaseFiling resource, String method) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Restituisce il package della classe
	 * 
	 * @return package
	 */
	private String getThisPackage() {
		return this.getClass().getPackage().getName();
	}

}
