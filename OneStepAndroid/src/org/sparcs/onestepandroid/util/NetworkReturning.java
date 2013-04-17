package org.sparcs.onestepandroid.util;

public class NetworkReturning {
	private int status;
	private String response;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public NetworkReturning(int status, String response) {
		super();
		this.status = status;
		this.response = response;
	}
	
}
