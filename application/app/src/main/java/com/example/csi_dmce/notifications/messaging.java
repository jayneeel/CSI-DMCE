package com.example.csi_dmce.notifications;

//import com.google.auth.oauth2.GoogleCredentials;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class messaging {


    public class OAuthTokenGetter {
        public static String getOAuthToken() {
            try {


                String serviceAccountFilePath = "assets/csi-dmce-c6f11-5cd215ce2ac6.json";
                // Load the service account credentials from the JSON key file
                FileInputStream serviceAccountStream = new FileInputStream(serviceAccountFilePath);
                GoogleCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);

                // Refresh the credentials to obtain an OAuth token
                credentials.refresh();
                // Get the access token
                String accessToken = credentials.getAccessToken().getTokenValue();

                // Close the input stream
                serviceAccountStream.close();

                // Return the OAuth token
                return accessToken;
            } catch (Exception e) {
                Log.d(TAG, "fcmTOKEN: error ");
                e.printStackTrace();
                Log.d(TAG, "fcmTOKEN:"+e);
                return null;
            }
        }

    }



}
