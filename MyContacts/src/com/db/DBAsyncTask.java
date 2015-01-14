package com.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
public class DBAsyncTask extends AsyncTask<Void, Void, Object> {
	public static final String TAG = DBAsyncTask.class.getSimpleName();
	private final Context context;
	String obj;
	String table_name;
	public DBAsyncTask(Context context2, String respStr,String table) {
		super();
		this.context = context2;
		this.obj=respStr;
		this.table_name=table;
	}
	
	@Override
	protected Object doInBackground(Void... params) {
		try {
			DBhandler dbHandler = DBhandler.getInstance(context);
			Log.i(TAG,"In rest async task response="+obj.toString() );
			dbHandler.bulkInsertIntoDB(obj,table_name);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
