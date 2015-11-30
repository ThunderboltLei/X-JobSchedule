package com.bfd.job.core.distrubed.proxy01;

import java.io.InputStream;
import java.io.PrintStream;

import com.innowhere.relproxy.jproxy.JProxyScriptEngine;

/**
 * @author: BFD474
 *
 * @description:
 */
public interface RelProxyBuiltin {

	public JProxyScriptEngine getJProxyScriptEngine();

	public void addOutputListener(OutputListener listener);

	public void removeOutputListener(OutputListener listener);

	public int getOutputListenerCount();

	public void addCommandListener(CommandListener listener);

	public void removeCommandListener(CommandListener listener);

	public int getCommandListenerCount();

	public void runLoop(InputStream in, PrintStream out);

}
