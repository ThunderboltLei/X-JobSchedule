package com.bfd.job.core.distrubed.proxy;

import com.bfd.job.core.distrubed.proxy.listener.CommandListener;
import com.bfd.job.core.distrubed.proxy.listener.TestRelProxyListener;
import com.innowhere.relproxy.RelProxyOnReloadListener;
import com.innowhere.relproxy.jproxy.JProxy;
import com.innowhere.relproxy.jproxy.JProxyCompilerListener;
import com.innowhere.relproxy.jproxy.JProxyConfig;
import com.innowhere.relproxy.jproxy.JProxyDiagnosticsListener;
import com.innowhere.relproxy.jproxy.JProxyInputSourceFileExcludedListener;
import com.innowhere.relproxy.jproxy.JProxyScriptEngine;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * @author: BFD474
 *
 * @description:
 */
public class RelProxyDoor {
	
	public static void main(String[] args) throws Exception {
		new RelProxyDoor();
	}

	public RelProxyDoor() {
		// http://stackoverflow.com/questions/3035351/broken-console-in-maven-project-using-netbeans
		// this is why is not a really JUnit test.
		setUp();
		try {
			mainTest();
		} finally {
			tearDown();
		}
		System.exit(0);
	}

	public void setUp() {
		URL res = this.getClass().getResource("/"); // .../target/classes/
		// Use example of RelProxy in development time:
		String inputPath = res.getFile() + "/../../src/main/java/";
		if (new File(inputPath).exists()) {
			System.out
					.println("RelProxy to be enabled, development mode detected");
		} else {
			System.out.println("RelProxy disabled, production mode detected");
			return;
		}
		JProxyInputSourceFileExcludedListener excludedListener = new JProxyInputSourceFileExcludedListener() {
			@Override
			public boolean isExcluded(File file, File rootFolderOfSources) {
				String absPath = file.getAbsolutePath();
				if (file.isDirectory()) {
					return absPath.endsWith(File.separatorChar
							+ "relproxy_builtin_ex");
				} else {
					return absPath.endsWith(File.separatorChar
							+ RelProxyDoor.class.getSimpleName() + ".java");
				}
			}
		};
		String classFolder = null; // Optional
		Iterable<String> compilationOptions = Arrays.asList(new String[] {
				"-source", "1.6", "-target", "1.6" });
		long scanPeriod = 1000;
		
		RelProxyOnReloadListener proxyListener = new RelProxyOnReloadListener() {
			@Override
			public void onReload(Object objOld, Object objNew, Object proxy,
					Method method, Object[] args) {
				System.out.println("Reloaded " + objNew + " Calling method: "
						+ method);
			}
		};
		
		JProxyCompilerListener compilerListener = new JProxyCompilerListener() {
			@Override
			public void beforeCompile(File file) {
				System.out.println("Before compile: " + file);
			}

			@Override
			public void afterCompile(File file) {
				System.out.println("After compile: " + file);
			}
		};
		
		JProxyDiagnosticsListener diagnosticsListener = new JProxyDiagnosticsListener() {
			@Override
			public void onDiagnostics(
					DiagnosticCollector<JavaFileObject> diagnostics) {
				List<Diagnostic<? extends JavaFileObject>> diagList = diagnostics
						.getDiagnostics();
				int i = 1;
				for (Diagnostic diagnostic : diagList) {
					System.err.println("Diagnostic " + i);
					System.err.println("  code: " + diagnostic.getCode());
					System.err.println("  kind: " + diagnostic.getKind());
					System.err.println("  line number: "
							+ diagnostic.getLineNumber());
					System.err.println("  column number: "
							+ diagnostic.getColumnNumber());
					System.err.println("  start position: "
							+ diagnostic.getStartPosition());
					System.err.println("  position: "
							+ diagnostic.getPosition());
					System.err.println("  end position: "
							+ diagnostic.getEndPosition());
					System.err.println("  source: " + diagnostic.getSource());
					System.err.println("  message: "
							+ diagnostic.getMessage(null));
					i++;
				}
			}
		};
		
		RelProxyBuiltin rpbRoot = RelProxyBuiltinSingleton.get();
		JProxyScriptEngine engine = rpbRoot.getJProxyScriptEngine();
		
		JProxyConfig jpConfig = JProxy.createJProxyConfig();
		jpConfig.setEnabled(true).setRelProxyOnReloadListener(proxyListener)
				.setInputPath(inputPath)
				.setJProxyInputSourceFileExcludedListener(excludedListener)
				.setScanPeriod(scanPeriod).setClassFolder(classFolder)
				.setCompilationOptions(compilationOptions)
				.setJProxyCompilerListener(compilerListener)
				.setJProxyDiagnosticsListener(diagnosticsListener);
		
		engine.init(jpConfig);
		System.out.println("RelProxy running");
	}

	public void tearDown() {
		RelProxyBuiltin rpbRoot = RelProxyBuiltinSingleton.get();
		JProxyScriptEngine engine = rpbRoot.getJProxyScriptEngine();
		engine.stop();
		System.out.println("RelProxy stopped");
	}

	public void mainTest() {
		RelProxyBuiltin rpbRoot = RelProxyBuiltinSingleton.get();
		
		TestRelProxyListener listener = new TestRelProxyListener();
		
//		rpbRoot.addOutputListener(listener);
//		assertTrue(rpbRoot.getOutputListenerCount() == 1);
//		
//		rpbRoot.removeOutputListener(listener);
//		assertTrue(rpbRoot.getOutputListenerCount() == 0);
//		
//		rpbRoot.addOutputListener(listener);
		
		CommandListener commandListener = listener.getCommandListener();
		
		rpbRoot.addCommandListener(commandListener);
		assertTrue(rpbRoot.getCommandListenerCount() == 1);
		
		rpbRoot.removeCommandListener(commandListener);
		assertTrue(rpbRoot.getCommandListenerCount() == 0);
		
		rpbRoot.addCommandListener(commandListener);
		rpbRoot.runLoop(System.in, System.out);
	}

	private static void assertTrue(boolean res) {
		if (!res)
			throw new RuntimeException("Unexpected Error");
	}
}
