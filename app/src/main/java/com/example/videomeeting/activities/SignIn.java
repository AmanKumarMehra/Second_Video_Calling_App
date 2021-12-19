package com.example.videomeeting.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.videomeeting.R;
import com.example.videomeeting.databinding.ActivitySignInBinding;
import com.example.videomeeting.utilities.Constants;
import com.example.videomeeting.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressBar signInProgressBar;
    PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.Key_Is_Signed_In)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.gotoSignUp.setOnClickListener(v -> startActivity(new Intent(SignIn.this, SignUp.class)));

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.inputEmail.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this, "Enter email address", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
                    Toast.makeText(SignIn.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }else if(binding.inputPassoword.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                }else{
                    signin();
                }
            }
        });

    }

    private void signin() {
        binding.buttonSignIn.setVisibility(View.INVISIBLE);
        binding.signInProgressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.Key_Collection_Users)
                .whereEqualTo(Constants.Key_Email, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.Key_Password, binding.inputPassoword.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            preferenceManager.putBoolean(Constants.Key_Is_Signed_In, true);
                            preferenceManager.putString(Constants.Key_User_Id, documentSnapshot.getId());
                            preferenceManager.putString(Constants.Key_First_Name, documentSnapshot.getString(Constants.Key_First_Name));
                            preferenceManager.putString(Constants.Key_Last_Name, documentSnapshot.getString(Constants.Key_Last_Name));
                            preferenceManager.putString(Constants.Key_Email, documentSnapshot.getString(Constants.Key_Email));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            signInProgressBar.setVisibility(View.INVISIBLE);
                            binding.buttonSignIn.setVisibility(View.VISIBLE);
                            Toast.makeText(SignIn.this, "Unable to SignIn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}