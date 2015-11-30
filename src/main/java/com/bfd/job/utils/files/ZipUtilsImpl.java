package com.bfd.job.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * @author: BFD474
 *
 * @description:
 */
public class ZipUtilsImpl implements FileUtils {
	
	private static final Logger logger = Logger.getLogger(ZipUtilsImpl.class);

	public static void readZIP(File zip) throws Exception {
		ZipFile zf = new ZipFile(zip);
		InputStream in = new BufferedInputStream(new FileInputStream(zip));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				continue;
			}
			logger.info("file - " + ze.getName() + " : " + ze.getSize()
					+ " bytes");
			long size = ze.getSize();
			if (size > 0) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						zf.getInputStream(ze)));
				String line;
				while ((line = br.readLine()) != null) {
					logger.info(line);
				}
				br.close();
			}
			logger.info("");
		}
		zin.closeEntry();
	}

	public static void main(String[] args) {

		try {
			ZipUtilsImpl
					.readZIP(new File("C://Users//BFD474//Desktop//my.zip"));
		} catch (Exception e) {
			logger.error(e.fillInStackTrace());
		}

	}

}
