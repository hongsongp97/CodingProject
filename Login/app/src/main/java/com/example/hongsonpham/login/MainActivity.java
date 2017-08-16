package com.example.hongsonpham.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
        image = (ImageView) findViewById(R.id.imageView);

        processLogin();
    }

    public void processLogin() {

//        if (accessToken != null) {
//            accessToken = com.facebook.AccessToken.getCurrentAccessToken();
//            System.out.println("logined");
//            LoginManager.getInstance().logOut();
//        }
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,picture.type(large),cover");
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            System.out.println(response);
                            String userDetail = response.getRawResponse();
                            try {
                                JSONObject jsonObject = new JSONObject(userDetail);
                                System.out.println("jsonObject::" + jsonObject);
                                String facebookIdText = jsonObject.getString("id");
                                facebookId.setText(facebookIdText);
                                String nameText = jsonObject.getString("name");
                                name.setText(nameText);
//                                String emailText = jsonObject.getString("email");
//                                email.setText(emailText);
                                String facebookImage = "https://graph.facebook.com/" + facebookIdText + "/picture?type=large";
                                System.out.println(facebookImage);
//                                String facebookCover = "https://graph.facebook.com/" + facebookIdText + "/photos";
//                                System.out.println(facebookCover);
//                                Picasso.with(getApplicationContext()).load(facebookCover).into(image);

                                String finalCoverPhoto = "";
                                if (jsonObject.has("cover")) {
                                    String getInitialCover = jsonObject.getString("cover");

                                    if (getInitialCover.equals("null")) {
                                        jsonObject = null;
                                    } else {
                                        JSONObject JOCover = jsonObject.optJSONObject("cover");

                                        if (JOCover.has("source")) {
                                            finalCoverPhoto = JOCover.getString("source");
                                        } else {
                                            finalCoverPhoto = null;
                                        }
                                    }
                                } else {
                                    finalCoverPhoto = null;
                                }
                                System.out.println(finalCoverPhoto);
                                Picasso.with(getApplicationContext()).load(finalCoverPhoto).into(image);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).

                executeAsync();

        new

                GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "https://graph.facebook.com/1136561069821009?fields={fieldname_of_type_CoverPhoto}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback()

                {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).

                executeAsync();
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
