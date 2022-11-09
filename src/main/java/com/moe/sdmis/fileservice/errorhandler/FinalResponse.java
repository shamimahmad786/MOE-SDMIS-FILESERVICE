package com.moe.sdmis.fileservice.errorhandler;

public class FinalResponse {

	private String responseData;
	private String status;
	private String message;
	public FinalResponse(String responseData, String status, String message) {
		super();
		this.responseData = responseData;
		this.status = status;
		this.message = message;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
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
	
	public FinalResponse FinalResponse(String responseData, String status, String message) {
	return new FinalResponse(responseData,status,message);
	}
	
	
}
