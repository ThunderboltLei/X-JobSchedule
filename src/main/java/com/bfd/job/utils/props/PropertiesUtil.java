package com.bfd.job.utils.props;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author: BFD474
 *
 * @description:
 */
public class PropertiesUtil {
	
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
	
	public static Properties props //
	; //

	static {
		try {
			props = new Properties();
			props.load(PropertiesUtil.class.getClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Load configuraiton file error...", e);
		}
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties props) {
		PropertiesUtil.props = props;
	}
}
