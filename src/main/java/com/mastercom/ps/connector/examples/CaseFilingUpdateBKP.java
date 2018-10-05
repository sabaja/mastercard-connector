package com.mastercom.ps.connector.examples;

/**
*
* Script-Name: update_case_sample
*/

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.*;
import com.mastercard.api.core.model.*;
import com.mastercard.api.core.model.map.*;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.mastercom.*;

import java.io.*;
import java.util.Map;

class CaseFilingUpdateBKP {

 public static void main(String[] args) throws Exception {

   String consumerKey = "your consumer key";   // You should copy this from "My Keys" on your project page e.g. UTfbhDCSeNYvJpLL5l028sWL9it739PYh6LU5lZja15xcRpY!fd209e6c579dc9d7be52da93d35ae6b6c167c174690b72fa
   String keyAlias = "keyalias";   // For production: change this to the key alias you chose when you created your production key
   String keyPassword = "keystorepassword";   // For production: change this to the key alias you chose when you created your production key
   InputStream is = new FileInputStream("path to your .p12 private key file"); // e.g. /Users/yourname/project/sandbox.p12 | C:\Users\yourname\project\sandbox.p12
   ApiConfig.setAuthentication(new OAuthAuthentication(consumerKey, is, keyAlias, keyPassword));   // You only need to set this once
   ApiConfig.setDebug(true);   // Enable http wire logging
   // This is needed to change the environment to run the sample code. For production: use ApiConfig.setSandbox(false);
   ApiConfig.setEnvironment(Environment.parse("sandbox"));

   try {
     RequestMap map = new RequestMap();
     map.set("case-id", "536092");
     map.set("fileAttachment.filename", "test.tif");
     map.set("fileAttachment.file", "sample file");
     map.set("action", "REJECT");
     map.set("memo", "This is a test memo");
     CaseFiling response = new CaseFiling(map).update();

     out(response, "caseId"); //-->536092

   } catch (ApiException e) {
     err("HttpStatus: "+e.getHttpStatus());
     err("Message: "+e.getMessage());
     err("ReasonCode: "+e.getReasonCode());
     err("Source: "+e.getSource());
   }
 }

 public static void out(SmartMap response, String key) {
   System.out.println(key+"-->"+response.get(key));
 }

 public static void out(Map<String,Object> map, String key) {
   System.out.println(key+"--->"+map.get(key));
 }

 public static void err(String message) {
   System.err.println(message);
 }
}