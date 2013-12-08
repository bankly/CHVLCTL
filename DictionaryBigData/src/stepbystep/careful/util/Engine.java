package stepbystep.careful.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import stepbystep.careful.DictApplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Engine {

	private DatabaseIndexInfo dbHelper;
	private SQLiteDatabase database;
	private Context mContext;
	private DictApplication mDictApp;

	public Engine(Context context) {
		dbHelper = new DatabaseIndexInfo(context);
		mContext = context;
		mDictApp = (DictApplication) mContext.getApplicationContext();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void openDict(String folder) throws SQLException {
		String dbPath = DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
		        + File.separator + folder;
		File dbFile = new File(dbPath, DictApplication.FILE_DB);
		database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null,
		        SQLiteDatabase.OPEN_READWRITE);
	}

	public void openDict1(String folder) throws SQLException {
		String dbPath = DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
		        + File.separator + folder;
		File dbFile = new File(dbPath, "anh_viet.db");
		database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null,
		        SQLiteDatabase.OPEN_READWRITE);
	}

	public void updateUnsignedWord() {
		int count = 1;
		String word;
		database.beginTransaction();
		while (count <= 390163) {
			Cursor cursorWord = database.rawQuery("SELECT" + " word" + " FROM" + " viet_anh"
			        + " where rowid = " + count, null);

			if (cursorWord.moveToFirst()) {

				word = RemoveSignedVN.removeAccent(cursorWord.getString(0));

				ContentValues values = new ContentValues();
				values.put("unsigned_word", word);
				int result = database.update("viet_anh", values, "rowid = " + count, null);
				cursorWord.close();
				count++;

			}
		}
		database.setTransactionSuccessful();
		database.endTransaction();

	}

	public String getEntry(String value) {
		String result;
		Cursor cursor = database.rawQuery("SELECT " + "content" + " FROM " + "viet_anh" + " WHERE "
		        + "word" + " = ? ", new String[] { value }, null);

		if (cursor == null) {
			return null;
		}

		if (cursor.moveToFirst()) {
			result = cursor.getString(0);
			cursor.close();
			return result;
		} else {
			cursor.close();
			return null;
		}
	}

	public void getEntryAllContent() {
		int count = 1;
		File file = new File(DictApplication.PATH_SDCARD + File.separator
		        + DictApplication.FOLDER + File.separator + DictApplication.FOLDER_EV
		        + File.separator + "dict.dict");
		FileWriter fw = null;
		BufferedWriter bw = null;
		if (!file.exists()) {
			try {
	            file.createNewFile();
	            fw = new FileWriter(file, true);
	            bw = new BufferedWriter(fw, 4*1024*1024);
            } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
		}
		
		while (count <= 9461) {

			Cursor cursor = database.rawQuery("SELECT" + " content" + " FROM" + " anh_viet"
			        + " where rowid = " + count, null);
			if (cursor.moveToFirst()) {
				DictProtocol.convertDict(cursor.getString(0), bw);
				try {
	                bw.write("\n\n");
                } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }
				cursor.close();
				count++;
			}

		}
		
		try {
			
			Cursor cursor = database.rawQuery("SELECT" + " content" + " FROM" + " anh_viet"
			        + " where rowid = " + count, null);
			if (cursor.moveToFirst()) {
				DictProtocol.convertDict(cursor.getString(0), bw);
				cursor.close();
			}			
			
	        bw.flush();
	    	fw.flush();
			bw.close();
			fw.close();
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	
		

	}

	public void getEntryAllWord() {
		int count = 1;

		while (count <= 387513) {
			Cursor cursor = database.rawQuery("SELECT" + " word" + " FROM" + " anh_viet"
			        + " where rowid = " + count, null);
			if (cursor.moveToFirst()) {
				DictProtocol.convertIndex(cursor.getString(0));
				cursor.close();
				count++;
			}

		}

	}

	public void close() {
		if (database.isOpen()) {
			dbHelper.close();
		}
	}

	public int getWordCount() {
		if (mDictApp.getDictType().equals(DictApplication.arrDictType[0])) {
			return DictApplication.arrWordNumber[0];
		}

		if (mDictApp.getDictType().equals(DictApplication.arrDictType[1])) {
			return DictApplication.arrWordNumber[1];
		}

		return DictApplication.arrWordNumber[2];

	}

	private IndexInfo parseIndexInfo(Cursor cursor) {
		IndexInfo indexInfo = new IndexInfo();

		indexInfo.setWord(cursor.getString(0));
		indexInfo.setStart(cursor.getInt(1));
		indexInfo.setLength(cursor.getInt(2));

		return indexInfo;
	}

	public IndexInfo getMeaning(String word) {
		IndexInfo indexInfo = new IndexInfo();
		Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseIndexInfo.TABLE_NAME
		        + " WHERE " + DatabaseIndexInfo.COLUMN_WORD + " = ? and " + DatabaseIndexInfo.COLUMN_WORD + " collate binary = ? ", new String[] { word, word }, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			indexInfo = parseIndexInfo(cursor);
			cursor.close();
			return indexInfo;
		} else {
			return null;
		}
	}
	
	public IndexInfo getMeaningLoose(String word) {
		IndexInfo indexInfo = new IndexInfo();
		Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseIndexInfo.TABLE_NAME
		        + " WHERE " + DatabaseIndexInfo.COLUMN_WORD + " = ? " , new String[] { word }, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			indexInfo = parseIndexInfo(cursor);
			cursor.close();
			return indexInfo;
		} else {
			return null;
		}
	}

	public IndexInfo getMeaning(int position) {
		IndexInfo indexInfo = new IndexInfo();
		Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseIndexInfo.TABLE_NAME
		        + " WHERE rowid = " + position, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			indexInfo = parseIndexInfo(cursor);
			cursor.close();
			return indexInfo;
		} else {
			return null;
		}
	}

	public Object getEntryById(long i) {
		String result;
		Cursor cursor = database.rawQuery("SELECT " + DatabaseIndexInfo.COLUMN_WORD + " FROM "
		        + DatabaseIndexInfo.TABLE_NAME + " WHERE " + DatabaseIndexInfo.COLUMN_WORD_ID
		        + " = " + i, null);

		if (cursor == null) {
			return null;
		}

		if (cursor.moveToFirst()) {
			result = cursor.getString(0);
			cursor.close();
			return result;
		} else {
			cursor.close();
			return null;
		}
	}

	public int getEntryId(String value) {

		Cursor cursor = database.rawQuery("SELECT rowid " + " FROM " + DatabaseIndexInfo.TABLE_NAME
		        + " WHERE " + DatabaseIndexInfo.COLUMN_WORD + "  >= ? limit 1",
		        new String[] { value });

		if (cursor.moveToFirst()) {
			return cursor.getInt(0) - 1;
		} else {
			return -1;
		}

	}

	public int getEntryIdUnsigned(String value) {

		Cursor cursor = database.rawQuery("SELECT rowid " + " FROM " + DatabaseIndexInfo.TABLE_NAME
		        + " WHERE " + DatabaseIndexInfo.COLUMN_WORD_UNSIGNED + "  >= ? limit 1",
		        new String[] { value });

		if (cursor.moveToFirst()) {
			return cursor.getInt(0) - 1;
		} else {
			return -1;
		}

	}

	public int getEntryIdVnLang(String value) {

		Cursor cursor = database.rawQuery("SELECT rowid " + " FROM " + DatabaseIndexInfo.TABLE_NAME
		        + " WHERE " + DatabaseIndexInfo.COLUMN_WORD + "  >= ? limit 1",
		        new String[] { value });

		if (cursor.moveToFirst()) {
			return cursor.getInt(0) - 1;
		} else {
			return -1;
		}
	}
}
