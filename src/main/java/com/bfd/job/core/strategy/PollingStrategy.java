package com.bfd.job.core.strategy;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.bfd.job.JobScheduleDoor;

/**
 * @author: BFD474
 *
 * @description: 
 */
public class PollingStrategy implements Strategy {
	
	private static final Logger logger = Logger.getLogger(PollingStrategy.class);
	
	private static ConcurrentHashMap _SlaveMap_, //
	_MasterMap_; //
	
	static{
		_MasterMap_ = JobScheduleDoor._MasterStatus_;
		_SlaveMap_ = JobScheduleDoor._SlavesStatus_;
	}
	
	public PollingStrategy(){
		logger.info(JobScheduleDoor._SlavesStatus_.mappingCount());
	}

}
