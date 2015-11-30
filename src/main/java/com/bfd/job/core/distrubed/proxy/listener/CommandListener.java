package com.bfd.job.core.distrubed.proxy.listener;

import java.io.PrintStream;

/**
 * @author: BFD474
 *
 * @description:
 */
public interface CommandListener {

	public void execute(String command, String input, PrintStream out);

}
