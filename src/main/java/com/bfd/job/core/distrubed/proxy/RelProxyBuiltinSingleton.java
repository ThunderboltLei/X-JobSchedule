package com.bfd.job.core.distrubed.proxy;

import com.bfd.job.core.distrubed.proxy.impl.RelProxyBuiltinImpl;

/**
 * @author: BFD474
 *
 * @description:
 */
public class RelProxyBuiltinSingleton {
	
	private final static RelProxyBuiltinImpl SINGLETON = new RelProxyBuiltinImpl();

	public static RelProxyBuiltin get() {
		return (RelProxyBuiltin) SINGLETON;
	}
	
}
