/*
 * @fileName : ResponseHelper.java
 * @date : 2013. 5. 23.
 * @author : diaimm.
 * @desc : 
 */
package com.diaimm.april.commons.util;

import org.apache.commons.io.output.StringBuilderWriter;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * jaxb library를 통한 xml(json) &lt;--&gt; java bean 간의 변환용 util
 * 
 * @author diaimm
 */
public enum JaxbObjectMapper {
	JSON {
		private final ObjectMapper objectMapper = new ObjectMapper();
		{
			AnnotationIntrospector introspector = new AnnotationIntrospector.Pair(new JacksonAnnotationIntrospector(),
					new JaxbAnnotationIntrospector());
			objectMapper.setDeserializationConfig(objectMapper.getDeserializationConfig().withAnnotationIntrospector(introspector));
			objectMapper.setSerializationConfig(objectMapper.getSerializationConfig().withAnnotationIntrospector(introspector));
		}

		@Override
		public String stringify(Object data, Class<?> type) {
			try {
				OutputStream outputStream = new ByteArrayOutputStream();
				objectMapper.writeValue(outputStream, data);
				return outputStream.toString();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		@Override
		public <T> T objectify(String source, Class<T> type) {
			try {
				InputStream inputStream = new ByteArrayInputStream(source.getBytes());
				return objectMapper.readValue(inputStream, type);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		@Override
		public <T> T objectify(String source, TypeReference<T> type) {
			try {
				InputStream inputStream = new ByteArrayInputStream(source.getBytes());
				return objectMapper.readValue(inputStream, type);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	},

	XML {
		@Override
		public String stringify(Object data, Class<?> type) {
			try {
				JAXBContext newInstance = JAXBContext.newInstance(type);
				Marshaller marshaller = newInstance.createMarshaller();

				Writer writer = new StringBuilderWriter();
				marshaller.marshal(data, writer);
				return writer.toString();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T objectify(String source, Class<T> type) {
			try {
				JAXBContext newInstance = JAXBContext.newInstance(type);
				Unmarshaller unmarshaller = newInstance.createUnmarshaller();

				InputStream inputStream = new ByteArrayInputStream(source.getBytes());
				return (T) unmarshaller.unmarshal(inputStream);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T objectify(String source, TypeReference<T> type) {
			return objectify(source, (Class<T>) type.getType().getClass());
		}
	};

	/**
	 * 전달된 객체(data)를 type으로 지정된 형으로 인지하고, String(xml/json)화 합니다.
	 * 
	 * @param data
	 *            data가 담긴 instance
	 * @param type
	 *            data를 담고 있는 class의 type
	 * @return
	 */
	public abstract String stringify(Object data, Class<?> type);

	/**
	 * 지정된 source(String)을 type 형태의 instance로 unmarshall합니다.
	 * 
	 * @param source
	 * @param type
	 * @return
	 */
	public abstract <T> T objectify(String source, Class<T> type);

	/**
	 * 지정된 source(String)을 type 형태의 instance로 unmarshall합니다.
	 * 
	 * @param source
	 * @param type
	 * @return
	 */
	public abstract <T> T objectify(String source, TypeReference<T> type);
}
