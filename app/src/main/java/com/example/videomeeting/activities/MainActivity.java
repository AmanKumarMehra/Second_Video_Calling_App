package com.example.videomeeting.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videomeeting.R;
import com.example.videomeeting.adapter.UserAdapter;
import com.example.videomeeting.models.User;
import com.example.videomeeting.utilities.Constants;
import com.example.videomeeting.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private List<User> users;
    private UserAdapter userAdapter;
    private TextView textErrorMessage;
    private ProgressBar usersProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(String.format(
                "%s %s",
                preferenceManager.getString(Constants.Key_First_Name),
                preferenceManager.getString(Constants.Key_Last_Name)
        ));

        findViewById(R.id.textSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String newToken = task.getResult();
                sendFCMTokentoDatabase(newToken);
            }
        });

        RecyclerView usersRecyclerView = findViewById(R.id.userRecyclerView);
        textErrorMessage = findViewById(R.id.textErrorMessage);
        usersProgressBar = findViewById(R.id.userProgressBar);

        users = new ArrayList<>();
        userAdapter = new UserAdapter(users);
        usersRecyclerView.setAdapter(userAdapter);

        getUsers();
    }

    private void getUsers(){
        usersProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.Key_Collection_Users)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usersProgressBar.setVisibility(View.GONE);
                        String myUserId = preferenceManager.getString(Constants.Key_User_Id);
                        if(task.isSuccessful() && task.getResult() != null){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if(myUserId.equals(documentSnapshot.getId())){
                                    continue;
                                }
                                User user = new User();
                                user.firstName = documentSnapshot.getString(Constants.Key_First_Name);
                                user.lastName = documentSnapshot.getString(Constants.Key_Last_Name);
                                user.email = documentSnapshot.getString(Constants.Key_Email);
                                user.token = documentSnapshot.getString(Constants.Key_FCM_Token);
                                users.add(user);
                            }
                            if(users.size() > 0){
                                userAdapter.notifyDataSetChanged();
                            }else{
                                textErrorMessage.setText(String.format("%s", "No users available"));
                                textErrorMessage.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            textErrorMessage.setText(String.format("%s", "No users available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void sendFCMTokentoDatabase(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.Key_Collection_Users).document(
                preferenceManager.getString(Constants.Key_User_Id)
        );

        documentReference.update(Constants.Key_FCM_Token, token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Token Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Unable to send token" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public  void signOut(){
        Toast.makeText(MainActivity.this, "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.Key_Collection_Users).document(
                        preferenceManager.getString(Constants.Key_User_Id));

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.Key_FCM_Token, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                preferenceManager.clearPreference();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Unable to sign_out", Toast.LENGTH_SHORT).show();
            }
        });

    }
}














