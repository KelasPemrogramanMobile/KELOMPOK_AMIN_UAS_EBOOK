package com.odilogy.util;

import java.util.ArrayList;

import com.odilogy.ebookstore.object.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ebookstore";

	// Books table name
	private static final String TABLE_BOOKS_NAME = "books";

	// Contacts Table Columns names
	private static final String KEY_BOOK_ID = "id";
	private static final String KEY_BOOK_TITLE = "title";
	private static final String KEY_BOOK_DESCRIPTION = "description";
	private static final String KEY_BOOK_FILE = "file";
	private static final String KEY_BOOK_CATEGORY = "category";
	private static final String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_BOOKS_NAME + "("
			+ KEY_BOOK_ID + " INTEGER PRIMARY KEY," + KEY_BOOK_TITLE + " TEXT,"+ KEY_BOOK_CATEGORY +" TEXT, "
			+KEY_BOOK_DESCRIPTION+" TEXT,"+KEY_BOOK_FILE+" TEXT)";

	
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_BOOK_TABLE);		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS_NAME);
		// Create tables again
		onCreate(db);
	}

	// Adding new contact
	public long addBook(Item book) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_BOOK_ID, book.getId());
		values.put(KEY_BOOK_TITLE, book.getTitle());
		values.put(KEY_BOOK_DESCRIPTION, book.getDescription());
		values.put(KEY_BOOK_FILE, book.getFile());
		values.put(KEY_BOOK_CATEGORY, book.getMeta());		

		// Inserting Row
		long id = db.insert(TABLE_BOOKS_NAME, null, values);
		Log.v("DB DATA INIT", "id return "+id);
		db.close(); // Closing database connection
		return id;
	}

	// Getting single contact
	public Item getBook(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_BOOKS_NAME, new String[] { KEY_BOOK_ID, KEY_BOOK_TITLE,
				KEY_BOOK_DESCRIPTION,KEY_BOOK_FILE,KEY_BOOK_CATEGORY }, KEY_BOOK_ID + "=?",
				new String[] { String.valueOf(id) }, null, null,null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Item book = new Item(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
		// return contact
		cursor.close();
		db.close();
		return book;
	}

	// Getting All Books
	public ArrayList<Item> getAllBooks() {
		ArrayList<Item> bookList = new ArrayList<Item>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_BOOKS_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item book = new Item();
				book.setId(Integer.parseInt(cursor.getString(0)));
				book.setTitle(cursor.getString(1));
				book.setDescription(cursor.getString(2));
				book.setFile(cursor.getString(4));
				book.setMeta(cursor.getString(3));
				// Adding contact to list
				bookList.add(book);
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		return bookList;
	}

	// Getting contacts Count
	public int getBooksCount() {
		String countQuery = "SELECT  * FROM " + TABLE_BOOKS_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int numrows = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return numrows;
	}

	// Updating single contact
	public long updateBook(Item book) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_BOOK_TITLE, book.getTitle());
		values.put(KEY_BOOK_DESCRIPTION, book.getDescription());
		values.put(KEY_BOOK_FILE, book.getFile());
		values.put(KEY_BOOK_CATEGORY, book.getMeta());
		long id = db.update(TABLE_BOOKS_NAME, values, KEY_BOOK_ID + " = ?",
				new String[] { String.valueOf(book.getId()) }); 
		// updating row
		db.close();
		return id;
	}

	// Deleting single contact
	public void deleteBook(Item book) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BOOKS_NAME, KEY_BOOK_ID + " = ?",
				new String[] { String.valueOf(book.getId()) });
		db.close();
	}

	// Checking if book already exist
	public boolean isBookExist(int id) {
			SQLiteDatabase db = this.getReadableDatabase();
		   Cursor cursor = db.rawQuery("select 1 from "+TABLE_BOOKS_NAME+" where "+KEY_BOOK_ID+" = ?", new String[] { String.valueOf(id) });
		   boolean exists = (cursor.getCount() > 0);
		   cursor.close();
		   return exists;
	}
	
}
