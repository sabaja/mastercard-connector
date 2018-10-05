package com.mastercom.ps.connector.examples.tests;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.mastercom.ps.connector.response.domain.casefiling.FileAttachment;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MapToXMLTest {
	
	public static final String alias = "Alias";

	public static void main(String[] args) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "chris");
		map.put("island", "faranga");

		XStream magicApi = new XStream();
//		// clear out existing permissions and set own ones
//		xstream.addPermission(NoTypePermission.NONE);
//		// allow some basics
//		xstream.addPermission(NullPermission.NULL);
//		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
//		xstream.allowTypeHierarchy(Collection.class);
//		// allow any type from the same package
////		
//		xstream.allowTypesByWildcard(new String[] { "com.mastercom.ps.connector.**" });
		XStream.setupDefaultSecurity(magicApi);
		magicApi.registerConverter((Converter) new MapEntryConverter());
		magicApi.alias(alias, Map.class);

		String xml = magicApi.toXML(map);
		System.out.println("Result of tweaked XStream toXml()");
		System.out.println(xml);

		@SuppressWarnings("unchecked")
		Map<String, String> extractedMap = (Map<String, String>) magicApi.fromXML(xml);
		assert extractedMap.get("name").equals("chris");
		assert extractedMap.get("island").equals("faranga");

		
//		List<String> order = new ArrayList<>();
		// adds the same cd twice (two references to the same object)
		
		FileAttachment fileAttachment = new FileAttachment();
//		order.add(fileAttachment);

		XStream xstream = new XStream();
		xstream.alias(FileAttachment.class.getSimpleName(), FileAttachment.class);
		System.out.println(xstream.toXML(fileAttachment));
	}

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

				String key = reader.getNodeName(); // nodeName aka element's name
				String value = reader.getValue();
				map.put(key, value);

				reader.moveUp();
			}

			return map;
		}

	}

}