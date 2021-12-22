package com.example.videomeeting.utilities;

import java.util.HashMap;

public class Constants {
    public static final String Key_Collection_Users = "users";
    public static final String Key_First_Name = "first_name";
    public static final String Key_Last_Name = "last_name";
    public static final String Key_Email = "email";
    public static final String Key_Password = "password";
    public static final String Key_User_Id = "user_id";
    public static final String Key_FCM_Token = "fcm_token";

    public static final String Key_Preference_Name = "videoMeetingPreference";
    public static final String Key_Is_Signed_In = "isSignedIn";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";


    public static HashMap<String, String> getRemoteMessageHeaders(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAA4iNWnVk:APA91bHzujXayRVLAzoikRH_VWtD0F8F8VIEYGifg-bJt0RFB8H91ntXJ7Q8n4s3XgHY9LIYe2X21J7MdwCytaGPQAvMS0uA86XiTxaMX6QHkDlZGjEldBxbMLVU4GbNniEox2uXiDhG"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
