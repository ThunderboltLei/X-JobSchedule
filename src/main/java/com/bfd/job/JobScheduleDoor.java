package com.bfd.job;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeStatus;

import com.bfd.job.core.strategy.PollingStrategy;
import com.bfd.job.utils.props.PropertiesUtil;

/**
 * @author: BFD474
 *
 * @description:
 */
public class JobScheduleDoor {
	// logger
	private static final Logger logger = Logger
			.getLogger(JobScheduleDoor.class);

	private static final String _MASTER_ = "MASTER";
	private static final String _SLAVE_ = "SLAVES";

	private static final String hdfs; // 接收到的文件保存于HDFS目录下

	private static String master;
	private static String slave;

	/**
	 * key: ip, value: Integer( 0为异常，1为正常 )<br>
	 * example: <br>
	 * 192.168.8.82, 1<br>
	 * 192.168.8.83, 0<br>
	 */
	public static ConcurrentHashMap<String, Integer> _MasterStatus_ = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentHashMap<String, Integer> _SlavesStatus_ = new ConcurrentHashMap<String, Integer>();

	static {
		Properties prop = PropertiesUtil.getProps();
		hdfs = prop.getProperty("HDFS_ROOT_PATH");

		// 添加 MASTER / SLAVES 到缓存
		master = prop.getProperty(_MASTER_);
		slave = prop.getProperty(_SLAVE_);
		logger.info(master + ", " + slave);

		// 设置 master / slaves
		try {
			// 添加到NodesStatus中
			String[] masters = master.split(","), // 只单master
			slaves = slave.split(","); // slaves

			if (masters.length > 1) {
				throw new Exception(
						"PS: check more masters, please config only one master!");
			}
			if (slaves.length < 0) {
				throw new Exception("PS: please config more slaves!");
			}

			for (String m : masters) {
				if (!_MasterStatus_.keySet().contains(m)) {
					_MasterStatus_.put(m, 1);
				}
			}
			for (String s : slaves) {
				if (!_SlavesStatus_.keySet().contains(s)) {
					_SlavesStatus_.put(s, 1);
				}
			}
		} catch (Exception e) {
			logger.error("PS: Something Wrong~~~ " + e);
		}

	}

	// jobschedule
	public static void startJobSchedule() {
		new PollingStrategy();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JobScheduleDoor.startJobSchedule();

	}

}
