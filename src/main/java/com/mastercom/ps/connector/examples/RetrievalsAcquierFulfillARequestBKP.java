package com.mastercom.ps.connector.examples;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.Environment;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.model.map.SmartMap;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.mastercom.Retrievals;

class RetrievalsAcquierFulfillARequestBKP {

	public static void main(String[] args) throws Exception {
		final String P12 = "C:\\Users\\sabatinija\\Desktop\\Devspace\\PeopleSoft\\Mastercards\\MCD_Sandbox_MasterCom_API_TEST_API_Keys\\MasterCom_API_TEST-sandbox.p12";

		String consumerKey = "4zoJ6bSBi2I10kY2__njjwSB4YMaQIa7Xj0_OW2G7243f6b5!a6b6fa1d5324471b9bebb0e96f7ad0a00000000000000000"; // You
																																	// should
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
			map.set("claim-id", "200002020654");
			map.set("request-id", "2000020");
			map.set("acquirerResponseCd", "D");
			map.set("docTypeIndicator", "2");
			map.set("memo", "This is an example of what a memo could contain");
			map.set("fileAttachment.filename", "testimage111111.tif");
			map.set("fileAttachment.file", "1234567890");
			
			map.set("fileAttachment.filename", "testimage222222.tif");
			map.set("fileAttachment.file", "0987654321");
			
			Retrievals response = Retrievals.acquirerFulfillARequest(map);

			out(response, "requestId"); // -->300002296235

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
