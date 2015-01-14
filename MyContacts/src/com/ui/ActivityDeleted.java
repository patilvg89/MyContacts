package com.ui;

import java.util.ArrayList;
import java.util.List;
import com.adapter.AdapterDeletedContact;
import com.contact.virendra.R;
import com.db.DBhandler;
import com.model.Contact;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ActivityDeleted extends Activity {
	ListView contactListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_layout);
		contactListView = (ListView) findViewById(R.id.listViewContacts);
		List<Contact> contactList = new ArrayList<Contact>();
		contactList = DBhandler.getInstance(ActivityDeleted.this).getContactList("MyContact");
		
		AdapterDeletedContact adapter = new AdapterDeletedContact(ActivityDeleted.this, R.layout.deleted_contact_list_item, contactList);
		contactListView.setAdapter(adapter);
		
	}
}
