package com.example.hongsonpham.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessToken accessToken;
    TextView facebookId, email, name;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

//        String hashKey = getHashKey();
//        Log.d("HashKey::", hashKey);
        //1myRm+zep2ctb92vgcW9uSKm6y4=

        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        facebookId = (TextView) findViewById(R.id.facebookId);
        email = (TextView) findViewById(R.id.email);
        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);

        processLogin();
    }

    public void processLogin() {
        if (accessToken != null) {
            accessToken = com.facebook.AccessToken.getCurrentAccessToken();
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //check for granted permission
                System.out.println("Granted permission::" + loginResult.getRecentlyGrantedPermissions());
                //create a graph request to get the user detail
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String userDetail = response.getRawResponse();
                        try {
                            JSONObject jsonObject = new JSONObject(userDetail);
                            System.out.println("jsonObject::" + jsonObject);
                            String facebookIdText = jsonObject.getString("id");
                            facebookId.setText(facebookIdText);
                            String nameText = jsonObject.getString("name");
                            name.setText(nameText);
                            String emailText = jsonObject.getString("email");
                            email.setText(emailText);
                            String facebookImage = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
//                            Glide.with(getApplicationContext()).load(facebookImage).into(image);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

//                Bundle parameter = new Bundle();
//                parameter.putString("fields", "name, email");
//                graphRequest.setParameters(parameter);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("Network error");
            }
        });

//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile, email"));
//            }
//        });
    }

//    public String getHashKey() {
//        try {
//            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//        return "SHA-1 epic failed";
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
