package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private long lastClickTime = 0;
    private static final long DEBOUNCE_INTERVAL_MS = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // System Bar Insets Handling: Prevents status bar & gesture navigation bar overlap
        View scrollMain = findViewById(R.id.scrollMain);
        View layoutMainContent = findViewById(R.id.layoutMainContent);

        if (scrollMain != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollMain, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                if (layoutMainContent != null) {
                    layoutMainContent.setPadding(
                            layoutMainContent.getPaddingLeft(),
                            systemBars.top + dpToPx(16),
                            layoutMainContent.getPaddingRight(),
                            systemBars.bottom + dpToPx(24)
                    );
                }
                return insets;
            });
        }

        View btnStudentMode = findViewById(R.id.btnStudentMode);
        View btnDriverMode = findViewById(R.id.btnDriverMode);

        // Restrained subtle card entrance animations
        DecelerateInterpolator interpolator = new DecelerateInterpolator();
        if (btnStudentMode != null) {
            btnStudentMode.setAlpha(0f);
            btnStudentMode.setTranslationY(20f);
            btnStudentMode.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(450)
                    .setStartDelay(100)
                    .setInterpolator(interpolator)
                    .start();

            btnStudentMode.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < DEBOUNCE_INTERVAL_MS) return;
                lastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MainActivity.this, StudentMapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        if (btnDriverMode != null) {
            btnDriverMode.setAlpha(0f);
            btnDriverMode.setTranslationY(20f);
            btnDriverMode.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(450)
                    .setStartDelay(200)
                    .setInterpolator(interpolator)
                    .start();

            btnDriverMode.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < DEBOUNCE_INTERVAL_MS) return;
                lastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MainActivity.this, BeaconActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}