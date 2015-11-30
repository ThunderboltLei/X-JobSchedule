package com.bfd.job.utils.files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author: BFD474
 *
 * @description: tar.gz - LINUX
 */
public class TarGzUtilsImpl implements FileUtils {
	
	private final static Logger LOGGER = Logger.getLogger(TarGzUtilsImpl.class);

	// tar.gz - LINUX
	public static void readTARGZ(File targzFile) throws IOException {
		FileInputStream fileIn = null;
		BufferedInputStream bufIn = null;
		GZIPInputStream gzipIn = null;
		TarArchiveInputStream taris = null;

		try {
			/**
			 * initialize variables
			 */
			fileIn = new FileInputStream(targzFile);
			bufIn = new BufferedInputStream(fileIn);
			gzipIn = new GZIPInputStream(bufIn); // first unzip the input file
			taris = new TarArchiveInputStream(gzipIn);
			/**
			 * get each file from a loop
			 */
			TarArchiveEntry entry = null;
			while ((entry = taris.getNextTarEntry()) != null) {
				if (entry.isDirectory()) {
					continue;
				}
				LOGGER.info("----- " + entry);
				File file = entry.getFile();
				LOGGER.info(entry.getName() + ", " + entry.getSize());
				byte[] b = new byte[(int) entry.getSize()];
				taris.read(b);
				taris.read(b, 0, (int) entry.getSize());
				LOGGER.info(b.length);
				// LOGGER.info(b);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		} finally {
			taris.close();
			gzipIn.close();
			bufIn.close();
			fileIn.close();
		}
	}

	public static void main(String[] args) {
		try {
			TarGzUtilsImpl.readTARGZ(new File(
					"C://Users//BFD474//Desktop//my.zip"));
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

}
