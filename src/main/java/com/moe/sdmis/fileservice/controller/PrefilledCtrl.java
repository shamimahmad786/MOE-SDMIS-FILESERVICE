package com.moe.sdmis.fileservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.moe.sdmis.fileservice.feigninterface.APIClient;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = {"http://localhost:4200", "http://10.25.26.251:4200", "http://127.0.0.1:4200"}, allowedHeaders = "*")
//@CrossOrigin(origins = "http://example.com", maxAge = 3600)
@RequestMapping("/sdmis/prefilled")
public class PrefilledCtrl {
	
//	@Autowired
//	APIClient aPIClient;
	
//	 @Autowired
////	 @Autowired
//	    private RestTemplate restTemplate;
	
	
	@Value("${preFilledUrl}")
	private String preFilledUrl;
	
	@RequestMapping(value = "/downloadPrefillUdiseTemplate", method = RequestMethod.GET)
	public void downloadPrefillUdiseTemplate(@RequestParam("schoolId") String schoolId,@RequestParam("udiseCode") String udiseCode,HttpServletResponse response) throws Exception {
//		aPIClient.downloadPrefillUdiseTemplate(schoolId);
		System.out.println("called");
		HttpHeaders headers = new HttpHeaders();
//		System.out.println(restTemplate);
	
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		 ResponseEntity<byte[]> responseData  =restTemplate().exchange(preFilledUrl+schoolId, HttpMethod.GET,requestEntity, byte[].class);
	System.out.println("Response bytes--->"+responseData);
	 HttpServletResponse resp;
	 
	 String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=SDMS_Prefill_" +udiseCode+ ".xlsm";
		response.setHeader(headerKey, headerValue);
		response.setContentType("application/octet-stream");
	    
		response.getOutputStream().write(responseData.getBody());
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
