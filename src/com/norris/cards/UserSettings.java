package com.norris.cards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserSettings extends Activity {
	
	public void goBack(View view){
		Intent intent = new Intent(UserSettings.this, Dashboard.class);
		startActivity(intent);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
    }

}
