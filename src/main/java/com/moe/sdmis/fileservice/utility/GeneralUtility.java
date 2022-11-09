package com.moe.sdmis.fileservice.utility;

import java.util.HashMap;

import org.hibernate.mapping.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralUtility {

	public HashMap<String,String> CustomStringMapper(String data) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		 HashMap<String,String> obj=	 objectMapper.readValue(data, HashMap.class);
		return obj;
		
	}
}
