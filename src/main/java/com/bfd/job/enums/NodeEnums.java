package com.bfd.job.enums;

/**
 * @author: BFD474
 *
 * @description: 
 */
public enum NodeEnums {
	
	MASTER("MASTER"),//
	SLAVE("SLAVE");
	
	private String type; // type
	
	NodeEnums(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
