package com.example.vita.wat_ch.fraqments.auth;


import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vita.wat_ch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
	
	private LoginFragmentListener mListener;
	private Button login, register;
	private EditText email, password;
	
	public LoginFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		login = (Button) view.findViewById(R.id.btnLogin);
		login.setOnClickListener(this);
		
		register = (Button) view.findViewById(R.id.btnSingUpFrag);
		register.setOnClickListener(this);
		
		email = (EditText) view.findViewById(R.id.editTextEmailLogin);
		password = (EditText) view.findViewById(R.id.editTextPasswordLogin);
		return view;
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mListener = (LoginFragmentListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + "must implements ListenerLogin");
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnLogin :
				String email = this.email.getText().toString();
				String password = this.password.getText().toString();
				if (TextUtils.isEmpty(email)) {
					this.email.setError(getString(R.string.email_is_empty));
					break;
				} else if (TextUtils.isEmpty(password)) {
					this.password.setError(getString(R.string.password_is_empty));
					break;
				} else if (password.length() < 6) {
					this.password.setError(getString(R.string.password_to_short));
					break;
				}
				FirebaseAuth mAuth = FirebaseAuth.getInstance();
				mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()){
							return;
						}
						else{
							Toast.makeText(getActivity(), "Email|Password is incorrect", Toast.LENGTH_LONG).show();
						}
						
					}
				});
				break;
			case R.id.btnSingUpFrag:
				mListener.onClickLogin();
				break;
		}
	}
	
	public interface LoginFragmentListener {
		// TODO: Update argument type and name
		void onClickLogin();
	}
}
