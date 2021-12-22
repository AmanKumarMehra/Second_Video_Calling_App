package com.example.videomeeting.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videomeeting.R;
import com.example.videomeeting.utilities.Constants;

public class IncomingInvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);

        ImageView imageMeetingType = findViewById(R.id.imageMeetingType);
        String meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);

        if(meetingType != null){
            if(meetingType.equals("video")){
                imageMeetingType.setImageResource(R.drawable.ic_video);
            }
        }

        TextView textFirstChar = findViewById(R.id.textFirstChar);
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textEmail = findViewById(R.id.textEmail);

        String firstname = getIntent().getStringExtra(Constants.Key_First_Name);
        if(firstname != null){
            textFirstChar.setText(firstname.substring(0, 1));
        }

        textUsername.setText(String.format(
                "%s %s",
                firstname,
                getIntent().getStringExtra(Constants.Key_Last_Name)
        ));

        textEmail.setText(getIntent().getStringExtra(Constants.Key_Email));
    }
}