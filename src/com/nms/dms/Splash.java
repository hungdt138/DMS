/**
 * 
 */
package com.nms.dms;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * @author Hung
 * 
 */
public class Splash extends BaseActivity {
	private ActionBar actionBar;
	ImageView vnn;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.hide();
	}

	@Override
	public void initView() {

		setContentView(R.layout.activity_splash);
		vnn = (ImageView) findViewById(R.id.vnn);
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(1500);
		vnn.startAnimation(aa);

		restoreUIState();

		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally {
					startActivity(new Intent(Splash.this, ActivityLogin.class));
					finish();
				}
			}
		}.start();
	}

	String FIRST_TIME = "firsttime";
	boolean isFirst = true;

	public void saveUIState() {

		SharedPreferences saveUI = getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = saveUI.edit();

		editor.putBoolean(FIRST_TIME, isFirst);
		editor.commit();
	}

	public void restoreUIState() {
		SharedPreferences savedUI = getPreferences(Activity.MODE_PRIVATE);
		isFirst = savedUI.getBoolean(FIRST_TIME, true);
	}

	@Override
	public void onPause() {
		super.onPause();
		saveUIState();
	}

}
