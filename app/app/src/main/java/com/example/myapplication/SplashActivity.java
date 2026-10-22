package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION_MS = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // System Bar Insets Handling: Prevents notch & navigation bar overlap
        View rootSplash = findViewById(R.id.rootSplash);
        if (rootSplash != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootSplash, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Animated Entrance Sequence
        View cardLogo = findViewById(R.id.cardLogo);
        View tvBrandTitle = findViewById(R.id.tvBrandTitle);
        View tvBrandSubtitle = findViewById(R.id.tvBrandSubtitle);
        View layoutStatusBadge = findViewById(R.id.layoutStatusBadge);
        View progressLoading = findViewById(R.id.progressLoading);

        DecelerateInterpolator interpolator = new DecelerateInterpolator();

        // Initial invisible states
        if (cardLogo != null) {
            cardLogo.setAlpha(0f);
            cardLogo.setTranslationY(30f);
            cardLogo.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(600)
                    .setInterpolator(interpolator)
                    .start();
        }

        if (tvBrandTitle != null) {
            tvBrandTitle.setAlpha(0f);
            tvBrandTitle.setTranslationY(20f);
            tvBrandTitle.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(550)
                    .setStartDelay(160)
                    .setInterpolator(interpolator)
                    .start();
        }

        if (tvBrandSubtitle != null) {
            tvBrandSubtitle.setAlpha(0f);
            tvBrandSubtitle.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setStartDelay(320)
                    .setInterpolator(interpolator)
                    .start();
        }

        if (layoutStatusBadge != null) {
            layoutStatusBadge.setAlpha(0f);
            layoutStatusBadge.animate()
                    .alpha(1f)
                    .setDuration(450)
                    .setStartDelay(450)
                    .setInterpolator(interpolator)
                    .start();
        }

        if (progressLoading != null) {
            progressLoading.setAlpha(0f);
            progressLoading.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .setStartDelay(550)
                    .setInterpolator(interpolator)
                    .start();
        }

        // Navigate to Role Selection Screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION_MS);
    }
}
