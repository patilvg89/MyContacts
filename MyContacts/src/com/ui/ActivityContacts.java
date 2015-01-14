package com.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.adapter.AdapterContactList;
import com.contact.virendra.R;
import com.model.Contact;

public class ActivityContacts extends Activity {
	ListView contactListView;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_layout);
		contactListView = (ListView) findViewById(R.id.listViewContacts);
		List<Contact> contactList = new ArrayList<Contact>();
		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
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

			System.out.println("Contact1 : " + Name + ", Number " + Number
					+ ", image_uri " + image_uri + " ,favorite: " + favorite);

			Contact c = new Contact();
			c.setName(Name);
			if (image_uri != null) {
				c.setImageUri(image_uri);
			}
			c.setFavorite(favorite);
			c.setNumber(Number);
			contactList.add(c);
		}
		AdapterContactList adapter = new AdapterContactList(
				ActivityContacts.this, R.layout.contact_list_item, contactList);
		contactListView.setAdapter(adapter);

		// set click listener
		contactListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Contact c = (Contact) view.getTag(R.string.app_name);
				// show option to perform with phone number
				ShowDialogOption(c.getNumber());
			}
		});
	}

	protected void ShowDialogOption(final String number) {
		AlertDialog.Builder abd = new AlertDialog.Builder(ActivityContacts.this);
		abd.setTitle("Choose Action to perform");
		abd.setNegativeButton("Message", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("sms_body", "This is my text to send.");
                startActivity(i);
			}
		});
		abd.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		abd.setPositiveButton("Call", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity((new Intent(Intent.ACTION_CALL)).setData(Uri
						.parse("tel:" + number)));
			}
		});
		abd.create();
		abd.show();
	}

}
