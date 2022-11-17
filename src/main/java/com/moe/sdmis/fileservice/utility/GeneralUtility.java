package com.moe.sdmis.fileservice.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.mapping.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralUtility {

	public HashMap<String,String> CustomStringMapper(String data) throws JsonMappingException, JsonProcessingException {
		System.out.println("JSON parse--->"+data);
		
		HashMap<String,String> impairmentObj= new HashMap<String , String>() {{
		    put("1",    "Blindness");
		    put("2", "Low-Vision");
		    put("3",   "Hearing impairment");
		    put("4",   "Speech and Language");
		    put("5",   "Locomotor Disability");
		    put("6",   "Mental illness");
		    put("7",   "Specific Learning Disabilities");
		    put("8",   "Cerebral palsy");
		    put("9",   "Autism Spectrum Disorder");
		    put("10",   "Multiple Disability incl. deaf, blindness");
		    put("11",   "Leprosy Cured students");
		    put("12",   "Dwarfism");
		    put("13",   "Intellectual Disability");
		    put("14",   "Muscular Dystrophy");
		    put("15",   "Chronic Neurological conditions");
		    put("16",   "Multiple Sclerosis");
		    put("17",   "Thalassemia");
		    put("18",   "Haemophilia");
		    put("19",   "Sickle Cell disease");
		    put("20",   "Acid Attack victim");
		    put("21",   "Parkinson’s disease");		    
		}};
		
		HashMap<String,String> finalJSON=new HashMap<String,String>();
		
		
		
//		impairmentObj
		
//		1-Blindness, 2-Low-Vision, 3-Hearing impairment, 4-Speech and Language,5-
//		Locomotor Disability, 6-Mental illness, 7-Specific Learning Disabilities, 8-Cerebral palsy, 9-Autism Spectrum
//		Disorder, 10-Multiple Disability incl. deaf, blindness, 11-Leprosy Cured students, 12-Dwarfism, 13-Intellectual
//		Disability, 14-Muscular Dystrophy, 15-Chronic Neurological conditions, 16-Multiple Sclerosis, 17-Thalassemia, 18-
//		Haemophilia, 19-Sickle Cell disease, 20-Acid Attack victim, 21-Parkinson’s disease
		
		
		
		List<String> elephantList = Arrays.asList(data.split(","));
		
		for(String imp:elephantList) {
			finalJSON.put(imp, impairmentObj.get(imp));
		}
		
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		 HashMap<String,String> obj=	 objectMapper.readValue(data, HashMap.class);
//		return obj;
		return finalJSON;
	}
	
	public HashMap<String,String> studentFacility(String uniform,String textbook) throws JsonMappingException, JsonProcessingException {
		HashMap<String,String> finalJSON=new HashMap<String,String>();
		
		if(uniform.equalsIgnoreCase("1"))
			finalJSON.put("1", "Free Text Book");
		if(textbook.equalsIgnoreCase("1"))
			finalJSON.put("2", "Free Uniforms");
		return finalJSON;
	}
}
