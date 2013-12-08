package stepbystep.careful.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import stepbystep.careful.DictApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class IndexInfoOperations {

	private DatabaseIndexInfo dbHelper;
	private String[] INDEXINFO_TABLE_COLUMNS = { DatabaseIndexInfo.COLUMN_WORD,
	        DatabaseIndexInfo.COLUMN_WORD_ID, DatabaseIndexInfo.COLUMN_START,
	        DatabaseIndexInfo.COLUMN_LENGTH };
	private SQLiteDatabase database;

	public IndexInfoOperations(Context context) {
		dbHelper = new DatabaseIndexInfo(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		setDatabase(database);
		
	}
	
	public void startTransaction() {
		database.beginTransaction();
	}
	
	public void endTransaction() {
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void openDict(String folder) throws SQLException {
		String dbPath = DictApplication.PATH_SDCARD + File.separator + DictApplication.FOLDER
		        + File.separator + folder;
		File dbFile = new File(dbPath, DictApplication.FILE_DB);
		database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null,
		        SQLiteDatabase.OPEN_READWRITE);
	}

	public void close() {
		if (database.isOpen()) {
			dbHelper.close();
		}
	}
	
	public void createDataBase(String folder) {
		try {
			String s;
			String[] arrString;
			open();
			BufferedRaf buffRaf = new BufferedRaf(new File(DictApplication.PATH_SDCARD
			        + "/" + DictApplication.FOLDER + "/" + folder + File.separator
			        + DictApplication.FILE_INDEX), "r");
			buffRaf.seek(0);

			Charset charset = Charset.forName("utf-8");
			String word;
						
			String sql = "INSERT INTO " + DatabaseIndexInfo.TABLE_NAME + " VALUES(?, ?, ?);";
			SQLiteStatement statement = database.compileStatement(sql);
			database.beginTransaction();			
			while ((s = buffRaf.readLine2()) != null) {

				arrString = s.split("\t");
				word = new String(arrString[0].getBytes("iso-8859-1"), charset);
				statement.clearBindings();
				statement.bindString(1, word);
				statement.bindLong(2, MyConvert.convertBase64ToDecimal(arrString[1]));
				statement.bindLong(3, MyConvert.convertBase64ToDecimal(arrString[2]));

				statement.execute();				
			}

			buffRaf.close();
			database.setTransactionSuccessful();
			database.endTransaction();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addIndexInfo(IndexInfo indexInfo) {

		ContentValues values = new ContentValues();
		values.put(DatabaseIndexInfo.COLUMN_WORD, indexInfo.getWord());
		values.put(DatabaseIndexInfo.COLUMN_START, indexInfo.getStart());
		values.put(DatabaseIndexInfo.COLUMN_LENGTH, indexInfo.getLength());
//		values.put(DatabaseIndexInfo.COLUMN_WORD_UNSIGNED, indexInfo.getWordUnsigned());

		database.insert(DatabaseIndexInfo.TABLE_NAME, null, values);
	}

	public void addIndexInfoUnsigned(IndexInfo indexInfo) {

		ContentValues values = new ContentValues();
		values.put(DatabaseIndexInfo.COLUMN_WORD, indexInfo.getWord());
		database.insert(DatabaseIndexInfo.TABLE_NAME, null, values);
	}

	public IndexInfo getIndexInfo(long value, String tableName) {
		Cursor cursor = database.query(tableName, INDEXINFO_TABLE_COLUMNS,
		        DatabaseIndexInfo.COLUMN_WORD_ID + " = ?", new String[] { Long.toString(value) },
		        null, null, null);

		if (cursor.moveToFirst()) {
			IndexInfo indexInfo = parseIndexInfo(cursor);
			return indexInfo;
		} else {
			return null;
		}
	}

	public IndexInfo getIndexInfo(String str, String tableName) {

		Cursor cursor = database.query(tableName, INDEXINFO_TABLE_COLUMNS,
		        DatabaseIndexInfo.COLUMN_WORD + " =  LIMIT 1", new String[] { str + "*" }, null,
		        null, null);

		if (cursor.moveToFirst()) {
			IndexInfo indexInfo = parseIndexInfo(cursor);
			return indexInfo;
		} else {
			return null;
		}
	}

	public IndexInfo getIndexInfoClick(String str, String tableName) {

		Cursor cursor = database.query(tableName, INDEXINFO_TABLE_COLUMNS,
		        DatabaseIndexInfo.COLUMN_WORD + " MATCH ?  LIMIT 1", new String[] { str }, null,
		        null, null);

		if (cursor.moveToFirst()) {
			IndexInfo indexInfo = parseIndexInfo(cursor);
			return indexInfo;
		} else {
			return null;
		}
	}

	public IndexInfo getIndexInfoUnsigned(String str, String tableName) {

		Cursor cursor = database.query(tableName, INDEXINFO_TABLE_COLUMNS,
		        DatabaseIndexInfo.COLUMN_WORD_ID + " = ?  LIMIT 1", new String[] { str + "*" },
		        null, null, null);
		if (cursor.moveToFirst()) {
			IndexInfo indexInfo = parseIndexInfo(cursor);
			return indexInfo;
		} else {
			return null;
		}
	}

	private IndexInfo parseIndexInfo(Cursor cursor) {
		IndexInfo indexInfo = new IndexInfo();
		
		indexInfo.setWord(cursor.getString(0));
		indexInfo.setStart(cursor.getInt(1));
		indexInfo.setLength(cursor.getInt(2));

		return indexInfo;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

}
