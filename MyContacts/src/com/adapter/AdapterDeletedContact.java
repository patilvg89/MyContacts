package com.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.contact.virendra.R;
import com.db.DBhandler;
import com.model.Contact;
import com.textdrawable.ColorGenerator;
import com.textdrawable.TextDrawable;

public class AdapterDeletedContact extends ArrayAdapter<Contact> {
	private Context context;
	private List<Contact> list;
	private int resource;
	private LayoutInflater inflater;
	AQuery aq;
	// declare the color generator and drawable builder
	private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
	private TextDrawable.IBuilder mDrawableBuilder;

	public AdapterDeletedContact(Context context, int resource,
			List<Contact> objects) {
		super(context, resource, objects);
		this.context = context;
		this.list = objects;
		this.resource = resource;
		aq = new AQuery(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (convertView == null) {
			view = inflater.inflate(resource, null);

			ViewHolder holder = new ViewHolder();
			holder.textName = (TextView) view
					.findViewById(R.id.textViewContactName);
			holder.textNumber = (TextView) view
					.findViewById(R.id.textViewContactNumber);
			holder.photoImage = (ImageView) view
					.findViewById(R.id.imageViewContact);
			holder.btnRestore = (Button) view.findViewById(R.id.buttonRestore);

			view.setTag(holder);
		} else {
			view = convertView;
		}
		mDrawableBuilder = TextDrawable.builder().round();
		final ViewHolder holder = (ViewHolder) view.getTag();

		final Contact c = list.get(position);

		holder.textName.setText(c.getName());
		holder.textNumber.setText(c.getNumber());
		// holder.photoImage.setImageURI(Uri.parse(c.getImageUri()));
		if (!c.getImageUri().equals("")) {
			holder.photoImage.setImageURI(Uri.parse(c.getImageUri()));
		} else {
			TextDrawable drawable = mDrawableBuilder.build(
					String.valueOf(c.getName().charAt(0)),
					mColorGenerator.getColor(c.getName()));
			holder.photoImage.setImageDrawable(drawable);
		}

		view.setTag(holder);
		view.setTag(R.string.app_name, c);

		holder.btnRestore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String DisplayName = c.getName();
				String MobileNumber = c.getNumber();
				String starred = c.getFavorite();

				ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

				ops.add(ContentProviderOperation
						.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
								null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME,
								null).build());

				// ------------------------------------------------------ Names
				if (DisplayName != null) {
					ops.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
									DisplayName).build());
				}

				// ------------------------------------------------- Mobile Number
				if (MobileNumber != null) {
					ops.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.Phone.NUMBER,
									MobileNumber)
							.withValue(
									ContactsContract.CommonDataKinds.Phone.TYPE,
									ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
							.build());
				}
				// ------------------------------------------------------
				// Favorite
				/*
				 * if (MobileNumber != null) { ops.add(ContentProviderOperation.
				 * newInsert(ContactsContract.Data.CONTENT_URI)
				 * .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
				 * 0)
				 * .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.
				 * CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				 * .withValue(ContactsContract.RawContacts.STARRED, starred)
				 * .withValue
				 * (ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract
				 * .CommonDataKinds.Phone.STARRED) .build()); }
				 */

				// Asking the Contact provider to create a new contact
				try {
					context.getContentResolver().applyBatch(
							ContactsContract.AUTHORITY, ops);
					DBhandler.getInstance(context).removeDeletedContact(
							c.getName(), c.getNumber(), "MyContact");
					list.remove(position);
					notifyDataSetChanged();

				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "Exception: " + e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

	}

	class ViewHolder {
		ImageView photoImage;
		Button btnRestore;
		TextView textName;
		TextView textNumber;
	}
}
