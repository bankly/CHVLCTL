package stepbystep.careful.util;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import android.util.Log;
import android.widget.Toast;
import stepbystep.careful.DictApplication;
import stepbystep.careful.MainActivity;

public class FileMeaning {

	public static String readMeaning(long start, long length, String folder) {

		byte[] buffer = new byte[(int) length];

		
		try {
			OptimizedRandomAccessFile oraf = new OptimizedRandomAccessFile(new File(DictApplication.PATH_SDCARD
	        	        + File.separator + DictApplication.FOLDER + File.separator + folder
	        	        + File.separator + DictApplication.FILE_MEANING), "r");
			oraf.seek(start);
			oraf.read(buffer);
			oraf.close();

//			InputStream is = new FileInputStream(new File(DictApplication.PATH_SDCARD
//			        + File.separator + DictApplication.FOLDER + File.separator + folder
//			        + File.separator + "dict.dict.gz"));
////
//			long s = System.currentTimeMillis();
//			GZIPInputStream gis = new GZIPInputStream(is);
//			long e = System.currentTimeMillis();
//			Log.i("MY", "gzip: " + (e-s));
//			RandomAccessStream ras = new RandomAccessStream(gis);
//			long ss = System.currentTimeMillis();
//			ras.seek(start);
//			
//			ras.read(buffer);
//			long ee = System.currentTimeMillis();
//			Log.i("MY", "load: " + (ee-ss));
//			
//			ras.close();
//			gis.close();
		
//			BufferedInputStream bis = new BufferedInputStream(gis, 65536);
//			long count = start;
//			
//			while (count > 0) {
//				count -= bis.skip(count);
//				bis.mark((int)count);
//				
//			}
//			bis.read(buffer);
//			bis.close();
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new String(buffer, Charset.forName("utf-8"));

	}
}
