package com.bfd.job.core.distrubed;

import com.bfd.job.enums.NodeEnums;

/**
 * @author: BFD474
 *
 * @description: 各个节点执行任务
 */
public class Task implements Runnable {

	private String MOrS = ""; // master or slave
	private String token = ""; // 用户TOKEN
	private String timestamp = ""; // 时间戳

	public Task(String MOrS, String token, String timestamp) {
		this.MOrS = MOrS;
		this.token = token;
		this.timestamp = timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// MASTER to do: emmit the task to slave
		if (NodeEnums.MASTER.getType().equals(MOrS)) {

		}
		// SLAVE to do: recieve the task from master
		else if (NodeEnums.SLAVE.getType().equals(MOrS)) {

		}
	}
}
