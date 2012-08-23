package com.android.screenoff;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
	private int timeout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
        	setBright(0.0f);
			timeout = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT);
			android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 0);
		} catch (SettingNotFoundException e) {
			Log.e("ERROR", e.toString());
		}
        
    }
    
    private void setBright(float value) {
    	Window myWindow = getWindow();
    	WindowManager.LayoutParams lp = myWindow.getAttributes();
    	lp.screenBrightness = value;
    	myWindow.setAttributes(lp);
    }

	@Override
	protected void onDestroy() {
		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, timeout);
		finish();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, timeout);
		finish();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_POWER:
				finish();
				return true;			
			default:
				return false;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
}