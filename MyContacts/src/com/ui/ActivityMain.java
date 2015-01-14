package com.ui;

import com.contact.virendra.R;
import com.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityMain extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main_activity);

		// register click listeners for button
		((Button) findViewById(R.id.btnContact)).setOnClickListener(this);
		((Button) findViewById(R.id.btnFavorite)).setOnClickListener(this);
		((Button) findViewById(R.id.btnDeleted)).setOnClickListener(this);
		
		//copy db to cache
		Util.copyDatabase(ActivityMain.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnContact:
			startActivity(new Intent(this, ActivityContacts.class));
			break;
		case R.id.btnFavorite:
			startActivity(new Intent(this, ActivityFavorites.class));
			break;
		case R.id.btnDeleted:
			startActivity(new Intent(this, ActivityDeleted.class));
			break;
		}
	}
}
