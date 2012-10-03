package fr.ydelouis.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import fr.ydelouis.commons.deletablelist.DeletableListDemoActivity;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startDeletableListDemo(View v) {
    	startActivity(new Intent(this, DeletableListDemoActivity.class));
    }
}
