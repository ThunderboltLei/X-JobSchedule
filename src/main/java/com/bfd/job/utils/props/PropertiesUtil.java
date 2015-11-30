package com.bfd.job.utils.props;

import java.util.Properties;

/**
 * @author: BFD474
 *
 * @description:
 */
public class PropertiesUtil {
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
