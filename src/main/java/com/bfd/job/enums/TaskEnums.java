package com.bfd.job.enums;

/**
 * @author: BFD474
 *
 * @description: 
 */
public enum TaskEnums {
	
	OnlinePackages("OnlinePackages"), // 在线数据包
	OfflinePackages("OfflinePackages"); // 离线数据包
	
	private String taskName;
	
	TaskEnums(String taskName){
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
