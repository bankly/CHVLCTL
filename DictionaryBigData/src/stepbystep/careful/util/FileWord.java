package stepbystep.careful.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import stepbystep.careful.DictApplication;

public class FileWord {

	Context context;

	public FileWord(Context context) {
		this.context = context;
	}

	public void createDataBase(String folder) {

		// read word
		try {
			String s;
			String[] arrString;

			IndexInfoOperations indexInfoOperations = new IndexInfoOperations(context);
			indexInfoOperations.open();
			RandomAccessFile randAccessFile = new RandomAccessFile(DictApplication.PATH_SDCARD
			        + "/" + DictApplication.FOLDER + "/" + folder + File.separator
			        + DictApplication.FILE_INDEX, "r");
			randAccessFile.seek(0);

			Charset charset = Charset.forName("utf-8");
			String word;

			SQLiteDatabase database;
			database = indexInfoOperations.getDatabase();
			String sql = "INSERT INTO " + DatabaseIndexInfo.TABLE_NAME + " VALUES(?, ?, ?);";
			SQLiteStatement statement = database.compileStatement(sql);
			indexInfoOperations.startTransaction();
			while ((s = randAccessFile.readLine()) != null) {

				arrString = s.split("\t");
				word = new String(arrString[0].getBytes("iso-8859-1"), charset);
				statement.clearBindings();
				statement.bindString(1, word);
				statement.bindLong(2, MyConvert.convertBase64ToDecimal(arrString[1]));
				statement.bindLong(3, MyConvert.convertBase64ToDecimal(arrString[2]));

				statement.execute();
				// IndexInfo indexInfo = new IndexInfo();
				//
				// word = new String(arrString[0].getBytes("iso-8859-1"), charset);
				//
				// indexInfo.setWord(word);
				// indexInfo.setStart(MyConvert.convertBase64ToDecimal(arrString[1]));
				// indexInfo.setLength(MyConvert.convertBase64ToDecimal(arrString[2]));
				// // indexInfo.setWordUnsigned(RemoveSignedVN.removeAccent(word));
				//
				// indexInfoOperations.addIndexInfo(indexInfo);
				
			}

			randAccessFile.close();
			indexInfoOperations.endTransaction();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
