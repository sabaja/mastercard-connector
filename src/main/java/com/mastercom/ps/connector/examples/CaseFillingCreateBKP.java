package com.mastercom.ps.connector.examples;
/**
*
* Script-Name: create_case_sample
*/

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.Environment;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.model.map.SmartMap;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.mastercom.CaseFiling;

class CaseFillingCreateBKP {

	public static void main(String[] args) throws Exception {

		final String P12 = "C:\\Users\\sabatinija\\Desktop\\Devspace\\PeopleSoft\\Mastercards\\MCD_Sandbox_MasterCom_API_TEST_API_Keys\\MasterCom_API_TEST-sandbox.p12";
		// You should copy this from "My Keys" on your project page e.g.
		// UTfbhDCSeNYvJpLL5l028sWL9it739PYh6LU5lZja15xcRpY!fd209e6c579dc9d7be52da93d35ae6b6c167c174690b72fa
		String CONSUMER_KEY = "your consumer key";
		// For production: change this to the key alias you chose when you created your
		// production key
		String KEY_ALIAS = "keyalias";
		// For production: change this to the key alias you chose when you created your
		// production key
		String KEY_PASSWORD = "keystorepassword";
		// e.g.
		// /Users/yourname/project/sandbox.p12
		// C:\Users\yourname\project\sandbox.p12
		InputStream is = new FileInputStream(P12);
		// You only need to set this once
		ApiConfig.setAuthentication(new OAuthAuthentication(CONSUMER_KEY, is, KEY_ALIAS, KEY_PASSWORD));
		// Enable http wire logging
		ApiConfig.setDebug(true); 
		
		// This is needed to change the environment to run the sample code. For
		// production: use ApiConfig.setSandbox(false);
		ApiConfig.setEnvironment(Environment.parse("sandbox"));

		try {
			RequestMap map = new RequestMap();
			map.set("caseType", "4");
			map.set("chargebackRefNum[0]", "1111423456, 2222123456");
			map.set("customerFilingNumber", "5482");
			map.set("violationCode", "D.2");
			map.set("violationDate", "2017-11-13");
			map.set("disputeAmount", "200.00");
			map.set("currencyCode", "USD");
			map.set("fileAttachment.filename", "test.tif");
			map.set("fileAttachment.file", "sample file");
			map.set("filedAgainstIca", "004321");
			map.set("filingAs", "A");
			map.set("filingIca", "001234");
			map.set("memo", "This is a test memo");
			CaseFiling response = CaseFiling.create(map);

			out(response, "caseId"); // -->536092

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