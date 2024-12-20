package com.example.quanlycongviec.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlycongviec.Auth.LoginActivity;
import com.example.quanlycongviec.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ImageView splashAvatar = findViewById(R.id.splashAvatar);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        splashAvatar.startAnimation(animation);

        new Handler().postDelayed(() -> {
            splashAvatar.clearAnimation();
            Intent it = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }, 2000);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}