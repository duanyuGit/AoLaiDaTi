package com.yunmall.dati.job;

import android.view.accessibility.AccessibilityEvent;
import com.yunmall.dati.DaTiService;

public interface AccessbilityJob {
    String getTargetPackageName();
    void onCreateJob(DaTiService service);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
}
