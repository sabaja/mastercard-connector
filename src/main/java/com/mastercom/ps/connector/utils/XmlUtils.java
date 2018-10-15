package com.mastercom.ps.connector.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mastercard.api.core.model.RequestMap;
import com.mastercom.ps.connector.examples.tests.MapToXMLTest.MapEntryConverter;
import com.mastercom.ps.connector.exceptions.XmlUtilsException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Classe di Utilità per la gestione delle request in formato <code>xml</code>.
 * Crea un oggetto XmlUtils dal xml originale e estrapola informazioni
 * necessarie al processo/flusso del connettore, di seguito:
 * </p>
 * <ul>
 * <li>tagMethod - valore del tag method</li>
 * <li>method - metodo che fa riferimento all'end-point della risorsa
 * rest: retrieveDocumentation della classe CaseFiling</li>
 * <li>headName - (classe+metodo) della risorsa Rest e.g.:
 * CaseFilingRetrieveDocumentation</li>
 * <li>head - tag root</li>
 * <li>requestParameters - valori della risorsa Rest</li>
 * <li>end - tag chiusura</li>
 * <li>classe - nome della risorsa Rest e.g.: CaseFiling</li>
 * </ul>
 * 
 * <p>
 * 
 * @author SabatiniJa
 * @since 25/09/2018
 */
public final class XmlUtils {

	private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	/**
	 * Xml in input
	 */
	private final String xml;
	private final String headName;
	private final String tagMethod;
	private final String head;
	private final String requestParameters;
	private final String end;
	private final String classe;
	private final String method;

	/**
	 * 
	 * 
	 * @param xml
	 * @throws XmlUtilsException
	 *             - nel caso non sia valorizzato il tag tagMethod nel xml
	 */
	public XmlUtils(final String xml) throws XmlUtilsException {
		this.xml = this.removeExcapeSequenceAndBlankCharacters(xml);
		log.info("Request: " + this.xml);
		this.tagMethod = this.getTagMethod();
		log.info("Metodo: " + tagMethod);
		this.headName = this.createHeadName();
		log.info("HeadName: " + headName);
		this.head = this.createHead();
		log.info("Head tag:" + head);
		requestParameters = createRequestParameters();
		log.info("RequestParameters: " + ("".equals(requestParameters) ? "vuoto" : requestParameters));
		end = createEnd(headName);
		log.info("End tag: " + end);
		classe = getClasse();
		log.info("Classe di riferimento: " + classe);
		method = getMethod();
		log.info("Metodo di riferimento: " + method);
	}

	/**
	 * @return xml originale senza i caratteri di escape sequence
	 *         [<code>\n, \r</code>] e i caratteri spazio [<code>' '</code>]
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * Crea l'xml con i dati che andranno in pasto all'oggetto REST di Mastercard
	 * 
	 * @return Xml con i dati presenti nei tag <code>tagMethod</code> e
	 *         <code>RequestParameters</code> se presente
	 * @throws XmlUtilsException
	 */
	public String createRestObjectRequest() throws XmlUtilsException {
		String head = this.getHead();
		String requestParameters = this.getRequestParameters();
		String end = this.getEnd();
		String result = head + requestParameters + end;

		log.info("Xml-requestParameters: " + result);

		return result;
	}

	/**
	 * Estrapola il valore del tag <code>tagMethod</code> presente nell'input
	 * passato come paramentro
	 * 
	 * @return Il valore del tag <code>tagMethod</code>
	 * 
	 * @throws XmlUtilsException
	 *             Vengono lanciate eccezioni a fronte della presenza e della
	 *             valorizzazione del tag <code>tagMethod</code>
	 */
	public String getTagMethod() throws XmlUtilsException {
		isMethodParamPresents();
		String method = xml.substring(xml.indexOf("<method>") + 8, xml.indexOf("</method>"));
		return method;
	}

	/**
	 * Se presente il tag <code>RequestParameters</code> è presente, estrapola
	 * l'input passato come paramentro
	 * 
	 * @return Il valore del tag <code>RequestParameters</code> che nel caso sia
	 *         presente e valorizzato rappresenta i valori/parametri da passare alla
	 *         request Rest.
	 * @throws XmlUtilsException
	 * 
	 * 
	 */
	public String createRequestParameters() throws XmlUtilsException {
		isRequestParamsPresent();
		String requestParameters = xml.substring(
				(xml.indexOf("<RequestParameters>")) + ("<RequestParameters>".length()),
				xml.indexOf(("</RequestParameters>")));
		return requestParameters;
	}

	/**
	 * Produce il nome della risorsa Rest dal tag <code>tagMethod</code> e.g:
	 * <p>
	 * <ul>
	 * <li>il tag tagMethod: CaseFiling.retrieveDocumentation produrrà il nome -
	 * CaseFilingRetrieveDocumentation</li>
	 * </ul>
	 * 
	 * @return Il nome della risorsa Rest
	 * @throws XmlUtilsException
	 */
	private String createHeadName() throws XmlUtilsException {
		// String tagMethod = getMethod();
		StringBuilder strBuilder = new StringBuilder(tagMethod);
		// Uppercase primo carattere
		Character firstCharUpper = tagMethod.substring(0).toUpperCase().toCharArray()[0];
		// sostituzione
		strBuilder.setCharAt(0, firstCharUpper);
		Character upperCharAfterPoint = tagMethod.substring((tagMethod.indexOf(".") + 1)).toUpperCase()
				.toCharArray()[0];
		// Uppercase dopo punto
		strBuilder.setCharAt(tagMethod.indexOf(".") + 1, upperCharAfterPoint);
		// Sostituzione punto
		String headName = strBuilder.toString();
		return headName.replace(".", "");
	}

	/**
	 * Crea il tag <i>radice</i> del xml
	 * 
	 * @return tag <i>radice</i>
	 */
	public String createHead() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + this.headName + ">";
	}

	/**
	 * Crea il tag <i>radice</i> del xml tramite l'argomento passato
	 * 
	 * @param headName
	 *            - radice del xml
	 * @return Tag <i>radice</i>
	 */
	public String createHead(String headName) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<" + headName + ">";
	}

	/**
	 * Tramite l'argomento passato compone il tag di <i>chiusura</i> del xml
	 * 
	 * @param endName
	 *            - tag di chiusura xml
	 * @return tag di <i>chiusura</i>
	 */
	public static String createEnd(final String endName) {
		return "</" + endName + ">";
	}

	/**
	 * Verifica la presenza del tag <code>RequestParameters</code> nel xml di input
	 * 
	 * @return
	 *         <ul>
	 *         <li><code>false</code> se non presente</li>
	 *         <li><code>true</code> se presente</li>
	 *         </ul>
	 */
	private boolean isRequestParamsPresent() throws XmlUtilsException {
		boolean isRequestParamsPresent = false;
		if (xml.indexOf("<RequestParameters>") < 0) {
			String msg = "Nel xml in input non è presente il tag <RequestParameters>";
			log.error(msg);
			throw new XmlUtilsException(msg);
		} else {
			isRequestParamsPresent = true;
		}
		return isRequestParamsPresent;
	}

	/**
	 * Verifica la presenza del tag <code>tagMethod</code> nel xml di input
	 * 
	 * @return
	 *         <ul>
	 *         <li><code>false</code> se non presente</li>
	 *         <li><code>true</code> se presente</li>
	 *         </ul>
	 */
	private boolean isMethodParamPresents() throws XmlUtilsException {
		boolean isMethodParamsPresent = false;
		if (xml.indexOf("<method>") < 0) {
			String err = "Nel xml in input non è presente il tag  <method>";
			log.error(err);
			throw new XmlUtilsException(err);
		} else {
			isMethodParamsPresent = true;
		}
		return isMethodParamsPresent;
	}

	/**
	 * Parsing dell'input, trasforma l'argomento di tipo {@link String} in un tipo
	 * {@link Document}
	 * 
	 * @param xml
	 * @return xml in formato {@link Document}
	 */
	public Document parseStringToXML() {
		Document XML = null;
		try {
			XML = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException | IOException | ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		return XML;
	}

	/**
	 * Parsing dell'input, trasrforma l'argomento di tipo {@link Document} in un
	 * tipo {@link String}
	 * 
	 * @param doc
	 * @return xml in formato stringa
	 * @throws TransformerException
	 */
	public String parseXMLToString(final Document doc) throws TransformerException {
		StringWriter sw = new StringWriter();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.transform(new DOMSource(doc), new StreamResult(sw));
		return sw.toString();
	}

	/**
	 * Elimina i caratteri di escape sequence [<code>\n, \r</code>] e i caratteri
	 * spazio [<code>' ', '\u0000'</code>] dalla stringa in input tramite
	 * {@link java.lang.String#split(String) split}.
	 * 
	 * 
	 * @param xml
	 *            stringa xml
	 * @return stringa priva dei caratteri: <code>\n, \r, ' '</code>
	 */
	private String removeExcapeSequenceAndBlankCharacters(final String xml) {
		String[] arrS = xml.split("\t|\r|\n| ");
		String result = "";
		for (String s : arrS) {
			result += s;
		}
		return result;
	}

	/**
	 * Restituisce una stringa in formato xml con il contenuto delle coppie
	 * chiave/valori presenti nella mappa da converire in xml
	 * 
	 * @param map
	 *            contente i valori da convertire
	 * @param alias
	 *            nome di riferimento del tag radice del xml generato
	 * @return stringa con l'xml della mappa
	 */
	public String fromRequestMapToXmlConvert(final Map<String, Object> map, String alias) {
		XStream converter = new XStream();
		XStream.setupDefaultSecurity(converter);
		converter.registerConverter((Converter) new MapEntryConverter());
		converter.alias(alias, Map.class);
		return converter.toXML(map);
	}

	/**
	 * Restituisce una stringa contenente l'attributo headName.
	 * 
	 * @return attributo headName
	 */
	public String getHeadName() {
		return headName;
	}

	/**
	 * Restituisce una stringa contenente l'attributo head.
	 * 
	 * @return attributo head
	 */
	public String getHead() {
		return head;
	}

	/**
	 * Restituisce una stringa contenente l'attributo requestParameters.
	 * 
	 * @return attributo requestParameters
	 */
	public String getRequestParameters() {
		return requestParameters;
	}

	/**
	 * Restituisce una stringa contenente l'attributo end.
	 * 
	 * @return attributo end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * Restituisce una stringa contenente l'attributo classe rappresentante la
	 * risorsa Rest da instanziare. L'operazione prevede una conversione del primo
	 * carattere da minuscolo a maiuscolo.
	 * 
	 * @return attributo classe
	 * @throws XmlUtilsException
	 */
	public String getClasse() throws XmlUtilsException {
		// String tmp = this.getMethod();
		String tmp = this.tagMethod;
		String method = tmp.replace(tmp.charAt(0), Character.toUpperCase(tmp.charAt(0)));
		return method.substring(0, method.indexOf("."));
	}

	/**
	 * Restituisce una stringa contenente l'attributo method rappresentante
	 * l'end-point da invocare sulla risorsa Rest. L'operazione prevede una
	 * conversione del primo carattere da maiuscolo a minuscolo.
	 * 
	 * @return attributo method
	 * @throws XmlUtilsException
	 */
	public String getMethod() throws XmlUtilsException {
		// String tmp = this.getMethod();
		String tmp = this.tagMethod;
		String method = tmp.replace(tmp.charAt(0), Character.toLowerCase(tmp.charAt(0)));
		return method.substring(tmp.indexOf(".") + 1);
	}

	/**
	 * Classe innestata che rappresenta un'implementazione dell'interfaccia
	 * {@link Converter} per la conversione di tipo map/xml.
	 * 
	 * @author SabatiniJa
	 *
	 */
	public static class MapEntryConverter implements Converter {

		@SuppressWarnings("rawtypes")
		@Override
		public boolean canConvert(Class type) {
			return AbstractMap.class.isAssignableFrom(type);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

			AbstractMap<String, Object> map = (AbstractMap<String, Object>) value;
			for (Object obj : map.entrySet()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) obj;
				writer.startNode(entry.getKey().toString());
				Object val = entry.getValue();
				if (null != val) {
					writer.setValue(val.toString());
				}
				writer.endNode();
			}

		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

			Map<String, String> map = new HashMap<String, String>();

			while (reader.hasMoreChildren()) {
				reader.moveDown();

				// nodeName aka element's name
				String key = reader.getNodeName();
				String value = reader.getValue();
				map.put(key, value);

				reader.moveUp();
			}

			return map;
		}

	}

}
