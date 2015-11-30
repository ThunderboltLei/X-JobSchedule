package com.bfd.job.core.pool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author: BFD474
 *
 * @description: 
 */
public class JobSchedulePool extends BasePoolableObjectFactory<T> {
	
	private static final Logger logger = Logger.getLogger(JobSchedulePool.class);

	/* (non-Javadoc)
	 * @see org.apache.commons.pool.BasePoolableObjectFactory#makeObject()
	 */
	@Override
	public T makeObject() throws Exception {
		// TODO Auto-generated method stub
		logger.info("makeObject");
		return null;
	}

}
