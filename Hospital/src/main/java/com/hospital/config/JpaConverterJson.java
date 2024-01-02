package com.hospital.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JpaConverterJson implements AttributeConverter<Object, String>{
	
	@Autowired ObjectMapper objectMapper;
	
	@Override
	public String convertToDatabaseColumn(Object attribute) {
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new RuntimeException("unable convert object into string in JpaConverterJson");
		}
		
	}

	@Override
	public Object convertToEntityAttribute(String attribute) {
		
		try {
			
			  if(attribute == null) return null;
			  
		      Object obj = objectMapper.readValue(attribute, Object.class);
		      
		      return obj;
		    } catch (IOException ex) {
		      // logger.error("Unexpected IOEx decoding json from database: " + dbData);
		    	throw new RuntimeException("unable convert string into object in JpaConverterJson");
		    }
	}

}
