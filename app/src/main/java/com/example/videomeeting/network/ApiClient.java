package com.example.videomeeting.network;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static Retrofit retorfit = null;
    
    public static Retrofit getClient(){
        if(retorfit == null){
            retorfit = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/fcm/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }

        return retorfit;
    }
}
