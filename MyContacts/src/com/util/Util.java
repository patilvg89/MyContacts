package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class Util {
	public static void copyDatabase(Context context) {
		try {
			
			Context myContext = context.getApplicationContext();

			String DB_PATH = context.getApplicationContext().getCacheDir().getAbsolutePath();
			String DB_NAME = "mycontact.db";
			// DB_PATH =
			// Environment.getExternalStorageDirectory().getPath()+"/5thseptember/";
			Log.d("Test", "coping at " + DB_PATH);
			// Path to the just created empty db
			String outFileName = DB_PATH + "/" + DB_NAME;
			Log.d("Test", "outFileName= " + outFileName);
			// Open the empty db as the output stream
			File f = context.getDatabasePath(outFileName);
			if (!f.exists()) {
				// Open your local db as the input stream
				InputStream myInput = myContext.getAssets().open(DB_NAME);
				OutputStream myOutput = new FileOutputStream(outFileName);
				// transfer bytes from the inputfile to the outputfile
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
				Log.d("Test", "Copied data");
				// Close the streams
				myOutput.flush();
				myOutput.close();
				myInput.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
