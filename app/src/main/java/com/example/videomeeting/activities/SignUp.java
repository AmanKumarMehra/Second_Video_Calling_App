package com.example.videomeeting.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.videomeeting.databinding.ActivitySignUpBinding;
import com.example.videomeeting.utilities.Constants;
import com.example.videomeeting.utilities.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressBar signUpProgressBar;
    PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.gotoLogin.setOnClickListener(v -> onBackPressed());

        binding.Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.firstName.getText().toString().isEmpty()){
                    binding.firstName.setError("Enter First Name");
                }else if(binding.lastName.getText().toString().isEmpty()){
                    binding.lastName.setError("Enter Last Name");
                }else if(binding.email.getText().toString().isEmpty()){
                    binding.email.setError("Enter your Email");
                }else if(binding.password.getText().toString().isEmpty()){
                    binding.password.setError("Enter Password");
                }else if(binding.confirmPassowrd.getText().toString().isEmpty()){
                    binding.confirmPassowrd.setError("Enter confirm password");
                }else if(!binding.password.getText().toString().equals(binding.confirmPassowrd.getText().toString())){
                    binding.confirmPassowrd.setError("Confirm Password does not match");
                }else {
                    signUp();
                }
            }
        });

    }

    private void signUp() {
        binding.Signup.setVisibility(View.INVISIBLE);
        binding.signUpProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.Key_First_Name, binding.firstName.getText().toString());
        user.put(Constants.Key_Last_Name, binding.lastName.getText().toString());
        user.put(Constants.Key_Email, binding.email.getText().toString());
        user.put(Constants.Key_Password, binding.password.getText().toString());

        database.collection(Constants.Key_Collection_Users)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.Key_Is_Signed_In, true);
                    preferenceManager.putString(Constants.Key_User_Id, documentReference.getId());
                    preferenceManager.putString(Constants.Key_First_Name, binding.firstName.getText().toString());
                    preferenceManager.putString(Constants.Key_Last_Name, binding.lastName.getText().toString());
                    preferenceManager.putString(Constants.Key_Email, binding.email.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                 .addOnFailureListener(e -> {
            signUpProgressBar.setVisibility(View.INVISIBLE);
            binding.Signup.setVisibility(View.VISIBLE);
            Toast.makeText(SignUp.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}