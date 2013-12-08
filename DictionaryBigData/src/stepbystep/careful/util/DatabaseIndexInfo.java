package stepbystep.careful.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseIndexInfo extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "DICT";
	public static final String COLUMN_WORD = "WORD";
	public static final String COLUMN_WORD_UNSIGNED = "WORD_UNSIGNED";
	public static final String COLUMN_WORD_ID = "rowid";
	public static final String COLUMN_START = "START";
	public static final String COLUMN_LENGTH = "LENGTH";

	public static final String DATABASE_NAME = "dict.db";
	public static final int DATABASE_VERSION = 1;

	public static String DATABASE_PATH;

//	public static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_WORD
//	        + " collate nocase, " + COLUMN_START + "," + COLUMN_LENGTH + ", "
//	        + COLUMN_WORD_UNSIGNED + " collate nocase)";
	public static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_WORD
	        + " collate nocase, " + COLUMN_START + "," + COLUMN_LENGTH + ")";

	public DatabaseIndexInfo(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);

//		db.execSQL("CREATE INDEX " + TABLE_NAME + "_" + COLUMN_WORD + " ON " + TABLE_NAME + "("
//		        + COLUMN_WORD + ")");
//
//		db.execSQL("CREATE INDEX " + TABLE_NAME + "_" + COLUMN_WORD_UNSIGNED + " ON "
//		        + TABLE_NAME + "(" + COLUMN_WORD_UNSIGNED+ ")");

		 db.execSQL("CREATE INDEX  " + TABLE_NAME + "_" + COLUMN_WORD + " ON " + TABLE_NAME + "("
		 + COLUMN_WORD + ")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
