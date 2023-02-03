package com.moe.sdmis.fileservice.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.hibernate.mapping.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralUtility {

	public Integer[] CustomStringMapper(String data) throws JsonMappingException, JsonProcessingException {
		
		
		
		HashMap<String,String> impairmentObj= new HashMap<String , String>() {{
			put("0",    "0");
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
		
		Integer[] arrayObj=new Integer[elephantList.size()];
		
		for(String imp:elephantList) {
			finalJSON.put(imp, impairmentObj.get(imp));
		}
		
		for(int i=0;i<elephantList.size();i++) {
			arrayObj[i]=Integer.parseInt(elephantList.get(0));
		}
		
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		 HashMap<String,String> obj=	 objectMapper.readValue(data, HashMap.class);
//		return obj;
		return arrayObj;
	}
	
	public HashMap<String,String> studentFacility(String uniform,String textbook) throws JsonMappingException, JsonProcessingException {
		HashMap<String,String> finalJSON=new HashMap<String,String>();
		
		if(uniform.equalsIgnoreCase("1"))
			finalJSON.put("1", "Free Text Book");
		if(textbook.equalsIgnoreCase("1"))
			finalJSON.put("2", "Free Uniforms");
		return finalJSON;
	}
	

	 public static Map<String,String> catMapping=new HashMap<String,String>();
	 static {
	        HashMap<String,String> aMap = new HashMap<String,String>();
	        aMap.put("1","Primary");
	        aMap.put("2","Primary with Upper Primary");
	        aMap.put("3","Pr. With Up.Pr. Sec. and H.sec.");
	        aMap.put("4","Upper Primary only");
	        aMap.put("5","Up. Pr. Secondary and Higher Sec");
	        aMap.put("6","Pr. Up Pr. and Secondary only");
	        aMap.put("7","Upper Pr. and Secondary");
	        aMap.put("8","Secondary Only");
	        aMap.put("10","Secondary with Higher Secondary");
	        aMap.put("11","Higher Secondary only/Jr. College");
	        catMapping =  Collections.unmodifiableMap(aMap);
	    }
	 
	 public static Map<String,String> typeMapping=new HashMap<String,String>();
	 static {
	        HashMap<String,String> aMap = new HashMap<String,String>();
	        aMap.put("1","Boys Only");
	        aMap.put("2","Girls Only");
	        aMap.put("3","Co-Educational");

	        typeMapping =  Collections.unmodifiableMap(aMap);
	    }
	 
	 public static final List<String> INVALID_MOBILE_NUMBERS = new ArrayList<>(Arrays.asList("9000000000", "6000000000", "7000000000", "8000000000", "9876543210",
	            "9876543210", "9100000000", "9800000000", "9700000000", "9898989898", "9111111111", "9900000000", "9511111111",
	            "9812345678", "9400000000", "9500000000", "7800000000", "6500000000", "6111111111", "9123456789", "9000000000", "8888888888", "7777777777", "6666666666")) ;
	 
	 public static Map<String,Boolean> ImperialMapping=new HashMap<String,Boolean>();
	 static {
	        HashMap<String,Boolean> aMap = new HashMap<String,Boolean>();
	        aMap.put("1",true); 
	        aMap.put("2",true);
	        aMap.put("3",true); 
	        aMap.put("4",true);
	        aMap.put("5",true); 
	        aMap.put("6",true); 
	        aMap.put("7",true); 
	        aMap.put("8",true); 
	        aMap.put("9",true); 
	        aMap.put("10",true); 
	        aMap.put("11",true); 
	        aMap.put("12",true); 
	        aMap.put("13",true); 
	        aMap.put("14",true); 
	        aMap.put("15",true); 
	        aMap.put("16",true); 
	        aMap.put("17",true);
	        aMap.put("18",true); 
	        aMap.put("19",true); 
	        aMap.put("20",true); 
	        aMap.put("21",true);
	        ImperialMapping =  Collections.unmodifiableMap(aMap);
	    }
	 
	 public static Map<String,Integer> SectionMapping=new HashMap<String,Integer>();
	 static {
	        HashMap<String,Integer> aMap = new HashMap<String,Integer>();
	        aMap.put("A",1);
	        aMap.put("B",2);
	        aMap.put("C",3);
	        aMap.put("D",4);
	        aMap.put("E",5);
	        aMap.put("F",6);
	        aMap.put("G",7);
	        aMap.put("H",8);
	        aMap.put("I",9);
	        aMap.put("F",10);
	        aMap.put("K",11);
	        aMap.put("L",12);
	        aMap.put("M",13);
	        aMap.put("N",14);
	        aMap.put("O",15);
	        aMap.put("P",16);
	        aMap.put("Q",17);
	        aMap.put("R",18);
	        aMap.put("S",19);
	        aMap.put("T",20);
	        aMap.put("U",21);
	        aMap.put("V",22);
	        aMap.put("W",23);
	        aMap.put("X",24);
	        aMap.put("Y",25);
	        aMap.put("Z",26);
	        
	        SectionMapping =  Collections.unmodifiableMap(aMap);
	    }
	 
	 public Integer sectionId(String sec) {
		return SectionMapping.get(sec);
		 
	 }
	 
	 
}
