package com.moe.sdmis.fileservice.errorhandler;

import java.util.Map;

public class FinalResponse {

	private Map<String,Object> responseData;
	private String status;
	private String message;
	public FinalResponse(Map<String,Object> responseData, String status, String message) {
		super();
		this.responseData = responseData;
		this.status = status;
		this.message = message;
	}
	public Map<String,Object> getResponseData() {
		return responseData;
	}
	public void setResponseData(Map<String,Object> responseData) {
		this.responseData = responseData;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public FinalResponse FinalResponse(Map<String,Object> responseData, String status, String message) {
	return new FinalResponse(responseData,status,message);
	}
	
	
}
