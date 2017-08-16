package com.example.haihm.first_greeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.*;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginWithFacebook extends AppCompatActivity {

    CallbackManager callbackManager;
    String fbId, fbName, fbImage, fbCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_with_facebook);

//        String hashKey = getHashKey();
//        Log.d("HashKey::", hashKey);
        // 1myRm+zep2ctb92vgcW9uSKm6y4=

        loadData();
    }

    public void transfer() {
        Intent intent = new Intent(LoginWithFacebook.this, FirstGreetingMain.class);
        Bundle bund = new Bundle();
        bund.putString("fbName", fbName);
        bund.putString("fbImage", fbImage);
        bund.putString("fbCover", fbCover);
        intent.putExtra("MyPackage", bund);
        System.out.println("transfer");
        startActivity(intent);
    }

    public void loadData() {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,picture.type(large),cover");
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            String userDetail = response.getRawResponse();
                            try {
                                JSONObject jsonObject = new JSONObject(userDetail);
                                System.out.println("jsonObject::" + jsonObject);
                                fbId = jsonObject.getString("id");
                                fbName = jsonObject.getString("name");
                                fbImage = "https://graph.facebook.com/" + fbId + "/picture?type=large";

                                if (jsonObject.has("cover")) {
                                    String getInitialCover = jsonObject.getString("cover");

                                    if (getInitialCover.equals("null")) {
                                        jsonObject = null;
                                    } else {
                                        JSONObject JOCover = jsonObject.optJSONObject("cover");

                                        if (JOCover.has("source")) {
                                            fbCover = JOCover.getString("source");
                                        } else {
                                            fbCover = null;
                                        }
                                    }
                                } else {
                                    fbCover = null;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        transfer();
                    }
                }).executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

}
