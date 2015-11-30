package com.bfd.job.core.ResourceManage.resource;

import org.apache.log4j.Logger;

/**
 * @author: BFD474
 *
 * @description: ResourceNode 处理接收数据，基于NETTY通讯
 */
public class ResourceNode implements Runnable {

	private static final Logger logger = Logger.getLogger(ResourceNode.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		logger.info("This is ResourceNode...");
	}

}
