package com.example.vita.wat_ch;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vita.wat_ch.fraqments.drawer.AboutFragment;
import com.example.vita.wat_ch.fraqments.drawer.BusSelectionFragment;
import com.example.vita.wat_ch.fraqments.drawer.FeedbackFragment;
import com.example.vita.wat_ch.fraqments.drawer.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	
	AboutFragment fAbout;
	BusSelectionFragment fBusSelection;
	FeedbackFragment fFeedback;
	SettingsFragment fSettings;
	FragmentManager manager = getSupportFragmentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		fBusSelection = new BusSelectionFragment();
		fFeedback = new FeedbackFragment();
		fSettings = new SettingsFragment();
		fAbout = new AboutFragment();
		manager.beginTransaction().replace(R.id.container, fBusSelection).commit();
	}
	
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		
		if (id == R.id.bus_selection) {
			manager.beginTransaction().replace(R.id.container, fBusSelection).commit();
			
		} else if (id == R.id.settings) {
			manager.beginTransaction().replace(R.id.container, fSettings).commit();
			
		} else if (id == R.id.feedback) {
			manager.beginTransaction().replace(R.id.container, fFeedback).commit();
			
		} else if (id == R.id.about) {
			manager.beginTransaction().replace(R.id.container, fAbout).commit();
		} else if (id == R.id.exit) {
			FirebaseAuth mAuth = FirebaseAuth.getInstance();
			mAuth.signOut();
			startActivity(new Intent(this,AuthActivity.class));
			finish();
		}
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
