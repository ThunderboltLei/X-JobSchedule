package com.bfd.job.utils.files;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * @author: BFD474
 *
 * @description:
 */
public class RarUtilsImpl {
	
	private static final Logger logger = Logger.getLogger(RarUtilsImpl.class);

	public static void readRAR(File rar, File destDir) throws Exception {
		Archive archive = null;
		FileOutputStream fos = null;
		System.out.println("Starting...");
		
		try {
			archive = new Archive(rar);
			FileHeader fh = archive.nextFileHeader();
			int count = 0;
			File destFileName = null;
			while (fh != null) {
				System.out.println((++count) + ") " + fh.getFileNameString());
				String compressFileName = fh.getFileNameString().trim();
				destFileName = new File(destDir.getAbsolutePath() + "/"
						+ compressFileName);
				if (fh.isDirectory()) {
					if (!destFileName.exists()) {
						destFileName.mkdirs();
					}
					fh = archive.nextFileHeader();
					continue;
				}
				if (!destFileName.getParentFile().exists()) {
					destFileName.getParentFile().mkdirs();
				}
				fos = new FileOutputStream(destFileName);
				archive.extractFile(fh, fos);
				fos.close();
				fos = null;
				fh = archive.nextFileHeader();
			}

			archive.close();
			archive = null;
			System.out.println("Finished !");
		} finally {
			if (fos != null) {
				fos.close();
				fos = null;
			}
			if (archive != null) {
				archive.close();
				archive = null;
			}
		}
	}

	public static void main(String[] args) {
		try {
			RarUtilsImpl.readRAR(new File("C://Users//BFD474//Desktop//my.rar"),
					new File("C://Users//BFD474//Desktop//tmp"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
