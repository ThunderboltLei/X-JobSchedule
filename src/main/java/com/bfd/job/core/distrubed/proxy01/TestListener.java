package com.bfd.job.core.distrubed.proxy01;

import java.io.PrintStream;

/**
 * @author: BFD474
 *
 * @description:
 */
public class TestListener implements OutputListener {

	@Override
	public void write(PrintStream out) {
		out.println("uppercase");
		out.println("lowercase");
	}

	public CommandListener getCommandListener() {
		return new CommandListener() {
			@Override
			public void execute(String command, String text, PrintStream out) {
				if ("uppercase".equals(command))
					out.println(text.toUpperCase());
				else if ("lowercase".equals(command))
					out.println(text.toLowerCase());
				else
					out.println("Unknown command:" + command);
			}
		};
	}

}
