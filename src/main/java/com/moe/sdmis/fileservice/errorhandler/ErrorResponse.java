package com.moe.sdmis.fileservice.errorhandler;

/**
 * @author NIC
 *
 */
public class ErrorResponse {

private String errorMessage;
private String status;
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}



}
