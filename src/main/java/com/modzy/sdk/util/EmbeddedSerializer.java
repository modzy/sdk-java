package com.modzy.sdk.util;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.modzy.sdk.dto.EmbeddedData;

public class EmbeddedSerializer extends StdSerializer<EmbeddedData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6281183029679558216L;
	
	public EmbeddedSerializer() {
		super(EmbeddedData.class);
	}

	@Override
	public void serialize(EmbeddedData value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
		jgen.writeString("data:"+value.getMediaType()+";"+value.getEncoding()+","+DatatypeConverter.printBase64Binary(value.getData()));
		
	}

}
