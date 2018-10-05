package com.mastercom.ps.connector.examples;
/**
*
* Script-Name: case_filing_status_sample
*/

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.*;
import com.mastercard.api.core.model.*;
import com.mastercard.api.core.model.map.*;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.mastercom.*;

import java.io.*;
import java.util.List;
import java.util.Map;

class CaseFilingStatusBKP {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		final String P12 = "C:\\Users\\sabatinija\\Desktop\\Devspace\\PeopleSoft\\Mastercards\\MCD_Sandbox_MasterCom_API_TEST_API_Keys\\MasterCom_API_TEST-sandbox.p12";
		
		String consumerKey = "4zoJ6bSBi2I10kY2__njjwSB4YMaQIa7Xj0_OW2G7243f6b5!a6b6fa1d5324471b9bebb0e96f7ad0a00000000000000000"; // You should copy this from "My Keys" on your project page e.g.
													// UTfbhDCSeNYvJpLL5l028sWL9it739PYh6LU5lZja15xcRpY!fd209e6c579dc9d7be52da93d35ae6b6c167c174690b72fa
		String keyAlias = "keyalias"; // For production: change this to the key alias you chose when you created your
										// production key
		String keyPassword = "keystorepassword"; // For production: change this to the key alias you chose when you
													// created your production key
		InputStream is = new FileInputStream(P12); // e.g.
																					// /Users/yourname/project/sandbox.p12
																					// |
																					// C:\Users\yourname\project\sandbox.p12
		ApiConfig.setAuthentication(new OAuthAuthentication(consumerKey, is, keyAlias, keyPassword)); // You only need
																										// to set this
																										// once
		ApiConfig.setDebug(true); // Enable http wire logging
		// This is needed to change the environment to run the sample code. For
		// production: use ApiConfig.setSandbox(false);
		ApiConfig.setEnvironment(Environment.parse("sandbox"));

		try {
			RequestMap map = new RequestMap();
			map.set("caseFilingList[0].caseId", "536092");
			CaseFiling response = new CaseFiling(map).caseFilingStatus();

			out(response, "caseFilingResponseList[0].caseId"); // -->536092
			out(response, "caseFilingResponseList[0].status"); // -->COMPLETED
			// This sample shows looping through caseFilingResponseList
			System.out.println("This sample shows looping through caseFilingResponseList");
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
		System.out.println(key + "-->" + response.get(key));
	}

	public static void out(Map<String, Object> map, String key) {
		System.out.println(key + "--->" + map.get(key));
	}

	public static void err(String message) {
		System.err.println(message);
	}
}