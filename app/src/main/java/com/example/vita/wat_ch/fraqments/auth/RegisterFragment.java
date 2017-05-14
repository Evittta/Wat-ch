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


public class RegisterFragment extends Fragment implements View.OnClickListener {
	
	private EditText email, password;
	private Button register, login;
	private RegisterFragmentListener mListener;
	
	public RegisterFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);
		email = (EditText) view.findViewById(R.id.editTextEmailSingUp);
		password = (EditText) view.findViewById(R.id.editTextPasswordSingUp);
		register = (Button) view.findViewById(R.id.btnRegister);
		register.setOnClickListener(this);
		
		login = (Button) view.findViewById(R.id.btnSingInFrag);
		login.setOnClickListener(this);
		return view;
	}
	
	
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof RegisterFragmentListener) {
			mListener = (RegisterFragmentListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
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
			case R.id.btnRegister:
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
				mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
					if (task.isSuccessful()){
						
					}else {
						Toast.makeText(getActivity(), "Email|Password is incorrect", Toast.LENGTH_LONG).show();
					}
					}
				});
				
				break;
			case R.id.btnSingInFrag:
				mListener.onClickRegister();
				break;
		}
	}
	
	public interface RegisterFragmentListener {
		// TODO: Update argument type and name
		void onClickRegister();
	}
}
