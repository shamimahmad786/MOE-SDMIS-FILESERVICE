package com.moe.sdmis.fileservice.errorhandler;

public class GenericExceptionHandler extends RuntimeException {

	private String errorMessage;
	private String errorCode;
	private String address;
	private String userId;
	private String schoolId;
	
	public GenericExceptionHandler(String errorMessage,String errorCode, String address, String userId, String schoolId) {
		super();
		this.errorMessage=errorMessage;
		this.errorCode=errorCode;
		this.address=address;
		this.userId=userId;
		this.schoolId=schoolId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	
	
	
	
	
//	public GenericExceptionHandler() {
//		super();
//	}

}
