package com.mastercom.ps.connector.examples.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XmlToObject {

	public String CaseFilingUpdate = "<?xml version=\"1.0\"?>" + "<request>"
			+ "             <method>caseFiling.update</method>" + "               <RequestParameters>"
			+ "                               <case-id>12345678</case-id>" + "<action>ACCEPT</action>"
			+ "                              <fileAttachment>" + "<filename>test.tiff</filename>"
			+ "<file>f6fd77vdfvuyfdvjfdnkjdfvjfdvjdfvkfdvdfvfvfd8v7fd87vdf8v78fdv78vf</file>" + "</fileAttachment>"
			+ "<memo>memo</memo>" + "<rebuttedAs>SND</rebuttedAs>" + "</RequestParameters>" + "</request>";

	public String xml = "<?xmlversion=\"1.0\"?><request><method>Chargebacks.acknowledgeReceivedChargebacks</method><RequestParameters><chargebackList><claimId>11111</claimId><ChargebackId>22222</ChargebackId></chargebackList><chargebackList><claimId>33333</claimId><ChargebackId>44444</ChargebackId></chargebackList></RequestParameters></request>";

	/**
	 * Da togliere e aggiungere i log a debug
	 * 
	 * @param args
	 */
	public static final String file = "C:\\Users\\sabatinija\\Desktop\\Devspace\\PeopleSoft\\Mastercards\\XML\\Request\\Transactions.RetrieveAuthorizationDetail.xml";

	public static void main(String[] args) {

		String value = "";
		try {
			value = new String(Files.readAllBytes(Paths.get(file)));
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("value: " + value);
		XmlToObject obj = new XmlToObject();
		// String normalizedString = obj.removeCarriageReturn(value);
		System.out.println(value);

		String method = obj.getMethod(value);
		System.out.println("method: " + method);

		String headName = obj.getHeadName(method);
		System.out.println("headName: " + headName);

		String head = obj.getHead(headName);
		System.out.println("head: " + head);

		String requestParameters = obj.getRequestParameters(value);
		System.out.println("params: " + requestParameters);

		String end = obj.getEnd(headName);
		System.out.println("end:" + end);

		String request = head + requestParameters + end;
		System.out.println("request: " + request);
	}

	public String removeCarriageReturn(String xml) {
		return xml.replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "");
	}

	public String getMethod(String xml) {
		return xml.substring(xml.indexOf("<method>") + 8, xml.indexOf("</method>"));
	}

	public String getRequestParameters(String xml) {
		return xml.substring((xml.indexOf("<RequestParameters>")) + ("<RequestParameters>".length()),
				xml.indexOf(("</RequestParameters>")));
	}

	public String getHeadName(String method) {
		StringBuilder strBuilder = new StringBuilder(method);
		// Uppercase primo carattere
		Character firstCharUpper = method.substring(0).toUpperCase().toCharArray()[0];
		System.out.println("firstCharUpper:" + firstCharUpper);
		// sostituzione
		strBuilder.setCharAt(0, firstCharUpper);
		System.out.println("passaggio 1 " + strBuilder);

		Character upperCharAfterPoint = method.substring((method.indexOf(".") + 1)).toUpperCase().toCharArray()[0];
		System.out.println("upperCharAfterPoint: " + upperCharAfterPoint);
		// Uppercase dopo punto
		strBuilder.setCharAt(method.indexOf(".") + 1, upperCharAfterPoint);
		System.out.println("passaggio 2 " + strBuilder);
		// Sostituzione vuoto a punto
		String headName = strBuilder.toString();
		return headName.replace(".", "");
	}

	public String getHead(String headName) {
		return "<?xml version=\"1.0\"?>" + "<" + headName + ">";
	}

	public String getEnd(String headName) {
		return "<" + headName + "/>";
	}
}

class XmlManagingException {

}
