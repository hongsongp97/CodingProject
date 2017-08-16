package com.example.haihm.first_greeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ForgetPassword extends AppCompatActivity {

    private ImageButton btnBackToMain;
    private Button fogetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnBackToMain = (ImageButton) findViewById(R.id.btnBackToMain);
        fogetPassword = (Button) findViewById(R.id.btnPassword);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
