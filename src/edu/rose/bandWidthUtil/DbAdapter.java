package edu.rose.bandWidthUtil;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	private static final String TAG = "SQLiteBandWidthAdapter"; // Just the tag
																// we
	// use to log

	private static final String DATABASE_NAME = "BandWidth.db"; // Becomes the
																// filename of
																// the database
	private static final String TABLE_NAME = "BandWidth"; // Only one table in
															// this
															// database
	private static final int DATABASE_VERSION = 7; // We increment this every
													// time we change the
													// database schema
													// which will kick off an
													// automatic upgrade

	private SQLiteOpenHelper mOpenHelper;
	private SQLiteDatabase mDb;

	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_RECEIVE = "received";
	public static final String KEY_SENT = "sent";

	private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	private static final String CREATE_STATEMENT;
	private static final String KEY_DATE = "date";
	static {
		StringBuilder s = new StringBuilder();
		s.append("CREATE TABLE " + TABLE_NAME + "(");
		s.append(KEY_ID + " integer primary key autoincrement, ");
		s.append(KEY_RECEIVE + " real, ");
		s.append(KEY_SENT + " real, ");
		s.append(KEY_DATE + " integer, ");
		s.append(KEY_NAME + " text");
		s.append(")");
		CREATE_STATEMENT = s.toString();
	}

	//Implement a SQLite database

	public DbAdapter(Context context) {
		mOpenHelper = new BandWidthDbHelper(context);
	}

	public void open() {
		mDb = mOpenHelper.getWritableDatabase();
	}

	public void close() {
		mDb.close();
	}

	/**
	 * Add a BandWidth to the table. Return the id if successful, -1 if not.
	 */

	public long addBandWidth(BandWidth BandWidth) {
		ContentValues row = getContentValuesFromBandWidth(BandWidth);
		return mDb.insert(TABLE_NAME, null, row);
	}

	private ContentValues getContentValuesFromBandWidth(BandWidth BandWidth) {
		ContentValues row = new ContentValues();
		row.put(KEY_NAME, BandWidth.getName());
		row.put(KEY_RECEIVE, BandWidth.getReceived());
		row.put(KEY_SENT, BandWidth.getSent());
		row.put(KEY_DATE, BandWidth.getDate());
		return row;
	}

	public Cursor getBandWidthsCursor() {
		String[] projection = new String[] { KEY_ID, KEY_NAME, KEY_RECEIVE,
				KEY_DATE, KEY_SENT };
		return mDb.query(TABLE_NAME, projection, null, null, null, null,
				KEY_RECEIVE + " DESC");
	}
	// get user's all bandwidth info
	public ArrayList<BandWidth> getBandWidth(String name) {

		ArrayList<BandWidth> res = new ArrayList<BandWidth>();

		String[] sta = { name };
		Cursor c1 = mDb.rawQuery("select * from BandWidth where " + KEY_NAME
				+ "=?", sta);
		c1.moveToFirst();
		while (true) {
			BandWidth s = new BandWidth();
			int idColumn = c1.getColumnIndexOrThrow(KEY_ID);
			s.setId(c1.getInt(idColumn));
			int nameColumn = c1.getColumnIndexOrThrow(KEY_NAME);
			s.setName(c1.getString(nameColumn));
			int ReceivedColumn = c1.getColumnIndexOrThrow(KEY_RECEIVE);
			s.setReceived(c1.getDouble(ReceivedColumn));

			int SentColumn = c1.getColumnIndexOrThrow(KEY_SENT);
			s.setSent(c1.getDouble(SentColumn));

			int dateColumn = c1.getColumnIndexOrThrow(KEY_DATE);
			s.setDate(c1.getInt(dateColumn));
			res.add(s);
			c1.moveToNext();
			if (c1.isAfterLast()) {

				break;
			}

		}
		return res;
	}
	//delete a user
	public void removeBandWidth(String name) {
		String where = KEY_NAME + " = " + "'" + name + "'";
		String[] whereArgs = null;
		mDb.delete(TABLE_NAME, where, whereArgs);

	}
	//delete an entry
	public void removeBandWidth(BandWidth BandWidth) {
		removeBandWidth(BandWidth.getName());
	}

	private static class BandWidthDbHelper extends SQLiteOpenHelper {

		public BandWidthDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Updating from version " + oldVersion + " to "
					+ newVersion
					+ ". This will destroy the database and re-create it.");
			db.execSQL(DROP_STATEMENT);
			onCreate(db);
		}
	}
}
