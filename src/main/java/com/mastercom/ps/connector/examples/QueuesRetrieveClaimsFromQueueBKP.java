package com.mastercom.ps.connector.examples;

/**
 * QueuesRetrieveClaimsFromQueue
 * Script-Name: get_claims_from_queue_sample
 */

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.*;
import com.mastercard.api.core.model.*;
import com.mastercard.api.core.model.map.*;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.mastercom.*;

import java.io.*;
import java.util.Map;

class QueuesRetrieveClaimsFromQueueBKP {

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
			map.set("queue-name", "Closed");

			ResourceList<Queues> responseList = Queues.retrieveClaimsFromQueue(map);
			Queues response = responseList.getList().get(0);

			out(response, "acquirerId"); // -->00000005195
			out(response, "acquirerRefNum"); // -->05103246259000000000126
			out(response, "primaryAccountNum"); // -->52751494691484000
			out(response, "claimId"); // -->200002020654
			out(response, "clearingDueDate"); // -->2017-11-13
			out(response, "clearingNetwork"); // -->GCMS
			out(response, "createDate"); // -->2017-11-13
			out(response, "dueDate"); // -->2017-11-13
			out(response, "transactionId"); // -->118411681
			out(response, "isAccurate"); // -->TRUE
			out(response, "isAcquirer"); // -->TRUE
			out(response, "isIssuer"); // -->FALSE
			out(response, "isOpen"); // -->TRUE
			out(response, "issuerId"); // -->5258
			out(response, "lastModifiedBy"); // -->user1234
			out(response, "lastModifiedDate"); // -->2017-11-08
			out(response, "merchantId"); // -->0024038000200
			out(response, "progressState"); // -->CB1-4807-O-A-NEW
			out(response, "claimType"); // -->Standard
			out(response, "claimValue"); // -->25.00 USD

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