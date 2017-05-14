package com.example.vita.wat_ch;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.example.vita.wat_ch.fraqments.auth.LoginFragment;
import com.example.vita.wat_ch.fraqments.auth.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AuthActivity extends AppCompatActivity implements RegisterFragment.RegisterFragmentListener,
		LoginFragment.LoginFragmentListener{
	
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	
	private FragmentManager fragmentManager = getSupportFragmentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		mAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					startActivity(new Intent(AuthActivity.this, DrawerActivity.class));
					finish();
					Log.d("q", "onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					fragmentManager.beginTransaction().replace(R.id.containerLogin, new LoginFragment()).commit();
					Log.d("q", "onAuthStateChanged:signed_out");
				}
				
			}
		};
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}
	
	@Override
	public void onClickRegister() {
		fragmentManager.beginTransaction().replace(R.id.containerLogin, new LoginFragment()).commit();
	}
	
	@Override
	public void onClickLogin() {
		fragmentManager.beginTransaction().replace(R.id.containerLogin, new RegisterFragment()).commit();
	}
}
