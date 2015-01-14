package com.db;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.model.Contact;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBhandler {
	private SQLiteDatabase db;

	public DBhandler(Context context) {
		db = SQLiteDatabase.openDatabase(context.getApplicationContext()
				.getCacheDir().getAbsolutePath()
				+ "/" + "mycontact.db", null, SQLiteDatabase.OPEN_READWRITE);
	}

	public void DBclose() {
		db.close();
	}

	static DBhandler dBhandler = null;

	public static DBhandler getInstance(Context context) {

		if (dBhandler == null) {
			dBhandler = new DBhandler(context);
		}
		return dBhandler;
	}

	// get countries
	public List<Contact> getContactList(String DB_TableName) {
		Log.i("TAG", "Inside getContactList");
		List<Contact> new_return_List = new ArrayList<Contact>();
		Cursor cursor;
		cursor = db.query(DB_TableName, null, null, null, null, null, null);
		System.out.println(cursor.getCount());

		if (cursor.moveToFirst()) {
			do {
				Contact contactObject = new Contact();
				contactObject.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				contactObject.setNumber(cursor.getString(cursor
						.getColumnIndex("number")));
				contactObject.setFavorite(cursor.getString(cursor
						.getColumnIndex("starred")));
				contactObject.setImageUri(cursor.getString(cursor
						.getColumnIndex("image_uri")));
				new_return_List.add(contactObject);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return new_return_List;
	}

	public void bulkInsertIntoDB(String obj, String TableName) {
		String sql = "INSERT INTO " + TableName + " VALUES (?,?,?,?);";
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
		try {
			JSONObject json = new JSONObject(obj);
			statement.clearBindings();
			statement.bindString(1, json.getString("name"));
			statement.bindString(2, json.getString("number"));
			statement.bindString(3, json.getString("starred"));
			statement.bindString(4, json.getString("image_uri"));
			statement.execute();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		db.setTransactionSuccessful();
		Log.i("TAG", "db size=" + db.getPageSize());
		db.endTransaction();
	}

	public void removeDeletedContact(String name, String number, String table) {
		try {
			db.delete(table, "name" + "=? AND " + "number" + "=?", new String[] {
					name, number });
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
