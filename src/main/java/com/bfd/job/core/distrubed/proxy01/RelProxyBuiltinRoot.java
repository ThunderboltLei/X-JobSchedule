package com.bfd.job.core.distrubed.proxy01;

import com.bfd.job.core.distrubed.proxy.impl.RelProxyBuiltinImpl;

/**
 * @author: BFD474
 *
 * @description:
 */
public class RelProxyBuiltinRoot {
	
	private final static RelProxyBuiltinImpl SINGLETON = new RelProxyBuiltinImpl();

	public static RelProxyBuiltin get() {
		return (RelProxyBuiltin) SINGLETON;
	}
	
}
