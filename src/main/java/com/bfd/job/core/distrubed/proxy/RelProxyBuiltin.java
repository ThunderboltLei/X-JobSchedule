package com.bfd.job.core.distrubed.proxy;

import java.io.InputStream;
import java.io.PrintStream;

import com.bfd.job.core.distrubed.proxy.listener.CommandListener;
//import com.bfd.job.core.distrubed.proxy.listener.OutputListener;
import com.innowhere.relproxy.jproxy.JProxyScriptEngine;

/**
 * @author: BFD474
 *
 * @description:
 */
public interface RelProxyBuiltin {

	public JProxyScriptEngine getJProxyScriptEngine();

	public void addCommandListener(CommandListener listener);

	public void removeCommandListener(CommandListener listener);

	public int getCommandListenerCount();

	public void runLoop(InputStream in, PrintStream out);

}
