package com.mastercom.ps.connector.examples.tests;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.model.map.SmartMap;
import com.mastercard.api.mastercom.CaseFiling;
import com.mastercom.ps.connector.config.ServiceConfiguration;

public class CaseFillingStatusReq {
	@SuppressWarnings("unused")
	private ServiceConfiguration config;
	private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private final String JSON = "{\r\n" + 
			"	\"caseFilingList\": [{\r\n" + 
			"		\"caseId\": \"536092\"\r\n" + 
			"	}, {\r\n" + 
			"		\"caseId\": \"536092\"\r\n" + 
			"	}]\r\n" + 
			"}";
	
	@SuppressWarnings("unchecked")
	public void send() {
		this.config = new ServiceConfiguration();
		try {
			RequestMap map = new RequestMap(JSON);
//			map.set("caseFilingList[0].caseId", "536092");
			CaseFiling response = new CaseFiling(map).caseFilingStatus();
			System.out.println(Objects.nonNull(response));
			out(response, "caseFilingResponseList[0].caseId"); // -->536092
			out(response, "caseFilingResponseList[0].status"); // -->COMPLETED
			// This sample shows looping through caseFilingResponseList
			log.info("This sample shows looping through caseFilingResponseList");
			for (Map<String, Object> item : (List<Map<String, Object>>) response.get("caseFilingResponseList")) {
				out(item, "caseId");
				out(item, "status");
			}

		} catch (ApiException e) {
			err("HttpStatus: " + e.getHttpStatus());
			err("Message: " + e.getMessage());
			err("ReasonCode: " + e.getReasonCode());
			err("Source: " + e.getSource());
		}
	}
	public static void out(SmartMap response, String key) {
		log.info(key + "-->" + response.get(key));
	}

	public static void out(Map<String, Object> map, String key) {
		log.info(key + "--->" + map.get(key));
	}

	public static void err(String message) {
		log.error(message);
	}
}
