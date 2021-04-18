package xyz.mgurga.jitter.utils;

import java.io.Serializable;

public class TAccount implements Serializable {
	private String handle;
	
	public TAccount(String handle) {
		this.handle = handle;
	}
	
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
}
