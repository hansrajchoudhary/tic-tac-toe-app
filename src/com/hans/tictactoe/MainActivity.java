package com.hans.tictactoe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainActivity extends Activity {
	RelativeLayout game101;
	private Game game1;
	private AdView adView;
	private TextView reset;
	private TextView title;
	private TextView fire_score;
	private TextView ie_score;
	int screen_width;
	int screen_height;
	RadioButton singleRadio;
	RadioButton twoRadio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game101 = (RelativeLayout) findViewById(R.id.game101);
		game1 = (Game) findViewById(R.id.game1);
        /***** Handle small screen device ******/
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		screen_height = metrics.heightPixels;
		screen_width = metrics.widthPixels;
		
		singleRadio = (RadioButton) findViewById(R.id.radioSingle);
		twoRadio = (RadioButton) findViewById(R.id.radioTwo);

		singleRadio.setText(R.string.radio_single);
		twoRadio.setText(R.string.radio_two);
		int tictactoeSize = screen_height>screen_width?((int)(screen_width*10)/15):((int)(screen_height)/2);
		game1.widthOfWindow = screen_width;
		game101.getLayoutParams().height = tictactoeSize;
		game101.getLayoutParams().width = tictactoeSize;
		game101.requestLayout();
		/**************/
		reset = (TextView) findViewById(R.id.reset);
		reset.setText(Html.fromHtml("<u><font color='red'>Reset!</font></u>"));
		reset.setTextSize(14);
		title = (TextView) findViewById(R.id.title);
		title.setText(Html.fromHtml("<u><font color='blue'>Firefox v/s IE!</font></u>"));
		title.setTextSize(15);
		Spinner spinner = (Spinner) findViewById(R.id.level);
		spinner.setOnItemSelectedListener(new MyItemSelectedListener(game1));
		List<String> list = new ArrayList<String>();
		list.add("Simple");
		list.add("Medium");
		list.add("Hard");
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);
		fire_score = (TextView) findViewById(R.id.firefox_score);
		ie_score = (TextView) findViewById(R.id.ie_score);
		game1.fireScore = fire_score;
		game1.ieScore = ie_score;
		fire_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>Firefox</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		ie_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>IE</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);

		ArrayAdapter<String> dataAdapter = null;
		dataAdapter= new ArrayAdapter<String>(this,R.layout.spinner_items, list);
		
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setSelection(1);
		if (screen_width >= 550) {
			reset.setTextSize(18);
			title.setTextSize(20);
			fire_score.setTextSize(18);
			ie_score.setTextSize(18);
		}
		//setContentView(R.layout.activity_main);
		adView = new AdView(this, AdSize.BANNER, "a150f3df5f29371");
		adView.loadAd(new AdRequest());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
	
	public void onResetClick(View view)
	{
		 fire_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>Firefox</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		 ie_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>IE</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		 game1.fireScoreVal = 0;
		 game1.ieScoreVal = 0;
		 game1.reset();
	}
	
	private class MyItemSelectedListener implements OnItemSelectedListener {
		private Game game1;
		public MyItemSelectedListener(Game game1) {
			this.game1 = game1;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			String level = parent.getItemAtPosition(pos).toString();
			if(game1.level != level)
			{
				game1.level = level;
				game1.reset();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		game101 = (RelativeLayout) findViewById(R.id.game101);
		game1 = (Game) findViewById(R.id.game1);
		/***** Handle small screen device ******/
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		screen_height = metrics.heightPixels;
		screen_width = metrics.widthPixels;
		int tictactoeSize = screen_height > screen_width ? ((int) (screen_width * 10) / 15)
				: ((int) (screen_height) / 2);
		game1.widthOfWindow = screen_width;
		game101.getLayoutParams().height = tictactoeSize;
		game101.getLayoutParams().width = tictactoeSize;
		game101.requestLayout();
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radioSingle:
	            if (checked && game1.getMode() != 1)
	            {
	            	fire_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>Firefox</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		       		ie_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>IE</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		       		game1.fireScoreVal = 0;
		       		game1.ieScoreVal = 0;
	            	game1.reset();
	            	game1.setMode(1);
	            }
	            break;
	        case R.id.radioTwo:
	            if (checked && game1.getMode() != 2)
	            {
	            	fire_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>Firefox</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		       		ie_score.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>IE</font></u><br/><br/>0</small></p>"), TextView.BufferType.SPANNABLE);
		       		game1.fireScoreVal = 0;
		       		game1.ieScoreVal = 0;
	            	game1.reset();
	            	game1.setMode(2);
	            }
	            break;
	    }
	}
	
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		
		super.onDestroy();
	}

	public void exitApp() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, About.class));
			return true;
		case R.id.like:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=com.hans.tictactoe")));
			return true;
		case R.id.exit:
			exitApp();
			return true;
		default:
			return true;
		}
	}
	
}
