package stepbystep.careful.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import stepbystep.careful.DictApplication;

import android.util.Log;

public class BinaryFile {
	public static void convertToBinFile(String folder) {
		try {
			OutputStream os = new DataOutputStream(new FileOutputStream(new File(
			        DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
			                + File.separator + folder + File.separator + "dict.bin"), true));

			OptimizedRandomAccessFile oraf = new OptimizedRandomAccessFile(new File(
			        DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
			                + File.separator + folder + File.separator + "dict.dict"), "r");
			String line;
			while ((line = oraf.readLine()) != null) {
				os.write(line.getBytes());
			}

			os.close();
			oraf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
