package fr.ydelouis.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import fr.ydelouis.commons.aftv.AutoFitTextViewDemoActivity;
import fr.ydelouis.commons.deletablelist.DeletableListDemoActivity;

public class MainActivity extends Activity
	implements
		OnClickListener
{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setListeners();
	}

	public void setListeners() {
		findViewById(R.id.main_deletableList).setOnClickListener(this);
		findViewById(R.id.main_autoFitTextView).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_deletableList:
				startActivity(new Intent(this, DeletableListDemoActivity.class));
				break;
			case R.id.main_autoFitTextView:
				startActivity(new Intent(this, AutoFitTextViewDemoActivity.class));
				break;
		}
	}
}
