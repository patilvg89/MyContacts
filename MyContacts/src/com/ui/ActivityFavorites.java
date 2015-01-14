package com.ui;

import java.util.ArrayList;
import java.util.List;

import com.adapter.AdapterContactList;
import com.adapter.AdapterFavoriteList;
import com.contact.virendra.R;
import com.model.Contact;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

public class ActivityFavorites extends Activity {
	ListView contactListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_layout);
		contactListView = (ListView) findViewById(R.id.listViewContacts);
		List<Contact> contactList = new ArrayList<Contact>();
		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,"starred=?",new String[] {"1"}, null);
		while (phones.moveToNext()) {
			String Name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String Number = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			String image_uri = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

			String favorite = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));

			System.out.println("Contact1 : " + Name + ", Number " + Number	+ ", image_uri " + image_uri + " ,favorite: " + favorite);

			Contact c = new Contact();
			c.setName(Name);
			if (image_uri != null) {
				c.setImageUri(image_uri);
			}
			c.setFavorite(favorite);
			c.setNumber(Number);
			contactList.add(c);
		}
		AdapterFavoriteList adapter = new AdapterFavoriteList(ActivityFavorites.this, R.layout.favorite_list_item, contactList);
		contactListView.setAdapter(adapter);
	}
}
