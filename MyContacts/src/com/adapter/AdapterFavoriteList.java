package com.adapter;

import java.util.List;
import com.androidquery.AQuery;
import com.contact.virendra.R;
import com.model.Contact;
import com.textdrawable.ColorGenerator;
import com.textdrawable.TextDrawable;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterFavoriteList extends BaseAdapter {

	private Context context;
	private List<Contact> list;
	private int resource;
	private LayoutInflater inflater;
	AQuery aq;
	// declare the color generator and drawable builder
	private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
	private TextDrawable.IBuilder mDrawableBuilder;

	public AdapterFavoriteList(Context context, int resource,
			List<Contact> objects) {
		super();
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
			holder.btnFavorite = (ImageView) view
					.findViewById(R.id.buttonFavorite);

			view.setTag(holder);
		} else {
			view = convertView;
		}

		mDrawableBuilder = TextDrawable.builder().round();

		final ViewHolder holder = (ViewHolder) view.getTag();

		final Contact c = list.get(position);

		holder.textName.setText(c.getName());
		holder.textNumber.setText(c.getNumber());

		if (!c.getImageUri().equals("")) {
			holder.photoImage.setImageURI(Uri.parse(c.getImageUri()));
		} else {
			TextDrawable drawable = mDrawableBuilder.build(
					String.valueOf(c.getName().charAt(0)),
					mColorGenerator.getColor(c.getName()));
			holder.photoImage.setImageDrawable(drawable);
		}

		holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);

		view.setTag(holder);
		view.setTag(R.string.app_name, c);

		holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// update fav contact
				updateFavUnFavStatus(0, c.getName());
				list.remove(position);
				notifyDataSetChanged();
			}
		});

		return view;
	}

	protected void updateFavUnFavStatus(int i, String name) {
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Contacts.STARRED, i);
		context.getContentResolver().update(Contacts.CONTENT_URI, values,
				Contacts.DISPLAY_NAME + "= ?", new String[] { name });
	}

	class ViewHolder {
		ImageView photoImage;
		ImageView btnFavorite;
		TextView textName;
		TextView textNumber;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Contact getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
