package com.yunmall.dati;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaTiApplication.activityCreateStatistics(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DaTiApplication.activityResumeStatistics(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DaTiApplication.activityPauseStatistics(this);
    }
}
