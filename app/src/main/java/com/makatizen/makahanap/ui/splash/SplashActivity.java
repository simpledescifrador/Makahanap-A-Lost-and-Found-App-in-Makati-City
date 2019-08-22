package com.makatizen.makahanap.ui.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.intro.IntroActivity;
import com.makatizen.makahanap.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
    }
}
