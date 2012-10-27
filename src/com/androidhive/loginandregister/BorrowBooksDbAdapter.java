package com.androidhive.loginandregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BorrowBooksDbAdapter {
	
	private static final String TAG = "SharedBooksDbAdapter";
	
	public static final String BOOK_TITLE = "booktitle";
	public static final String BOOK_AUTHOR = "bookauthor";
	public static final String SHARING_DATE = "sharingdate";
	public static final String BOOK_ISBN = "bookisbn";
	public static final String KEY_ID = "_id";
	
	private static final String DATABASE_NAME = "students2";
	private static final String DATABASE_TABLE = "borrowedbooks";
	private static final int DATABASE_VERSION = 3;
	
	private DatabaseHelper mDbHelper;
    public static SQLiteDatabase mDb;
	
	private static final String DATABASE_CREATE = 
			"create table borrowedbooks (_id integer primary key autoincrement,"
			+ "booktitle text not null, bookauthor text not null, sharingdate text not null, bookisbn text not null);";
			
	private final Context mCtx;
	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
		
	}
	
	public BorrowBooksDbAdapter(Context ctx){
		this.mCtx = ctx;
	}
	
	public BorrowBooksDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    public long createEntry(String title, String author, String sharingdate, String bookISBN) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(BOOK_TITLE, title);
        initialValues.put(BOOK_AUTHOR, author);
        initialValues.put(SHARING_DATE, "Date of Sharing: "+sharingdate); 
        initialValues.put(BOOK_ISBN, bookISBN);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteAll() {
        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    public Cursor fetchAllEntries() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ID,BOOK_TITLE, BOOK_AUTHOR,
                SHARING_DATE, BOOK_ISBN}, null, null, null, null, null);
    }
    public boolean deleteNote(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
    }

    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                    BOOK_TITLE, BOOK_AUTHOR, BOOK_ISBN}, KEY_ID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /*public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }*/

    /*public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }*/
}
