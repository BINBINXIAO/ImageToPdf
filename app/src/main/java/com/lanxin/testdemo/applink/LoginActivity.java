package com.lanxin.testdemo.applink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.lanxin.testdemo.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("njinfocenter://v1/app?lx_open_client=1&id=11927552&oid=6815744&url=http%3A%2F%2F180.101.236.117%3A8081%2Fphone%2F%3Flx_fullscreen%3Dtrue%23%2FMobileLayout%2FHomePage%2Fcomprehensive"));
                startActivity(intent);
            }
        });
    }
}