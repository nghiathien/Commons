package fr.ydelouis.commons.deletablelist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.ydelouis.commons.R;
import fr.ydelouis.commons.deletablelist.DeletableListView.OnItemDeletedListener;

public class DeletableListDemoActivity extends Activity
	implements
		OnItemDeletedListener
{
	private static final int[]	IMAGES	= new int[] {
					android.R.drawable.ic_menu_add,
					android.R.drawable.ic_menu_agenda,
					android.R.drawable.ic_menu_call,
					android.R.drawable.ic_menu_camera,
					android.R.drawable.ic_menu_close_clear_cancel,
					android.R.drawable.ic_menu_compass,
					android.R.drawable.ic_menu_crop };

	private ArrayList<String>	items	= new ArrayList<String>();
	private DeletableListView	list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deletablelist_demo);
		initList();
		list = (DeletableListView) findViewById(R.id.list);
		list.setAdapter(new CustomAdapter(this, items));
		list.setOnItemDeletedListener(this);
	}

	private void initList() {
		items.add("One");
		items.add("Two");
		items.add("Three");
		items.add("Four");
		items.add("Five");
		items.add("Six");
		items.add("Seven");
		items.add("Height");
		items.add("Nine");
		items.add("Ten");
		items.add("Eleven");
		items.add("Twelve");
		items.add("Thirteen");
		items.add("Fourteen");
		items.add("Fifteen");
	}

	@Override
	public void onItemDeleted(DeletableListView listView, int position) {
		items.remove(position);
		list.notifyDataSetChanged();
	}

	private class CustomAdapter extends ArrayAdapter<String>
	{
		public CustomAdapter(Context context, List<String> items) {
			super(context, R.layout.deletablelist_item, items);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null)
				view = View.inflate(getContext(), R.layout.deletablelist_item, null);

			((TextView) view.findViewById(R.id.deletableListItem_text)).setText(getItem(position));
			((ImageView) view.findViewById(R.id.deletableListItem_image)).setImageResource(IMAGES[position
					% IMAGES.length]);

			return view;
		}
	}
}
