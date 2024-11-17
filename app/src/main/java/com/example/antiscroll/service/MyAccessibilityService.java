package com.example.antiscroll.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.antiscroll.data.BlockScrollAppList;
import com.example.antiscroll.data.TimeTracking;
import com.example.antiscroll.db.TimeTrackingDatabase;
import com.example.antiscroll.repository.AvailableAppSettingRepository;
import com.example.antiscroll.repository.TimeTrackingRepository;
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel;
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModelFactory;
import com.example.antiscroll.viewmodel.TimeTrackingViewModel;
import com.example.antiscroll.viewmodel.TimeTrackingViewModelFactory;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService implements ViewModelStoreOwner {
    private final ViewModelStore viewModelStore = new ViewModelStore();
    private long startTime = 0;
    private long totalTime = 0;
    private boolean isTracking = false;
    private String currentApp = "";
    private TimeTrackingViewModel viewModel;
    private AvailableAppSettingViewModel availableAppSettingViewModel;
    private List<String> appList;
    private Handler handler;
    private static final long CHECK_INTERVAL = 1000; // 1 second interval for continuous check

    @Override
    public void onCreate() {
        super.onCreate();

        // Fetch the app list to be tracked
        appList = BlockScrollAppList.INSTANCE.getAppList();

        // Initialize the repositories and ViewModels for time tracking and app settings
        TimeTrackingRepository timeTrackingRepo = new TimeTrackingRepository(TimeTrackingDatabase.Companion.getDataBase(this).timeTrackingDao());
        TimeTrackingViewModelFactory timeTrackingFactory = new TimeTrackingViewModelFactory(timeTrackingRepo);
        viewModel = new ViewModelProvider(this, timeTrackingFactory).get(TimeTrackingViewModel.class);

        AvailableAppSettingRepository appSettingRepo = new AvailableAppSettingRepository(TimeTrackingDatabase.Companion.getDataBase(this).availableAppSettingDao());
        AvailableAppSettingViewModelFactory appSettingFactory = new AvailableAppSettingViewModelFactory(appSettingRepo);
        availableAppSettingViewModel = new ViewModelProvider(this, appSettingFactory).get(AvailableAppSettingViewModel.class);

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo nodeInfo = accessibilityEvent.getSource();
        if (nodeInfo == null) {
            stopTracking();
            return;
        }

        String foregroundApp = nodeInfo.getPackageName().toString();

        // Check if the current app is in the list of apps to block
        if (appList.contains(foregroundApp)) {
            // Get the anti-scroll setting for the app asynchronously using the Callback
            availableAppSettingViewModel.getIsAntiScrollEnabled(foregroundApp, new Callback<Integer>() {
                @Override
                public void onResult(Integer isEnabled) {
                    if (isEnabled == 1) {
                        checkAndTrackContent(nodeInfo, foregroundApp);
                    } else {
                        stopTracking();
                    }
                }
            });
        } else {
            stopTracking();
        }
    }

    // Check if the content is a reel and start tracking if needed
    private void checkAndTrackContent(AccessibilityNodeInfo node, String appPackage) {
        boolean isReelLayout = !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_video_container").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/reel_recycler").isEmpty();

        boolean isHomeLayout = !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_photo_profile_name").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/secondary_label").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_save").isEmpty();

        boolean isSearchLayout = !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/action_bar_search_edit_text").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/image_button").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/layout_container").isEmpty();

        boolean addLayout = !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/quick_capture_root_container").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/unified_camera_options_holder").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/layout_container_left").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/cam_desi_story").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/slideout_iconview_text").isEmpty();

        boolean profileLayout = !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/action_bar_large_title_auto_size").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/profile_tab_icon_view").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/profile_header_bio_text").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/profile_header_follow_button").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_save").isEmpty();

        boolean storyLayout = !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/message_composer_container").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/toolbar_like_button").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/toolbar_reshare_button").isEmpty()
                || !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/profile_picture_container").isEmpty();

        if (isReelLayout) {
            if (!isTracking) {
                startTracking(appPackage);
            }
        }  else if(isHomeLayout || isSearchLayout || addLayout || profileLayout || storyLayout) {
            stopTracking();


        }


    }

    // Start time tracking for the app
    private void startTracking(String appPackage) {
        startTime = System.currentTimeMillis();
        isTracking = true;
        currentApp = appPackage;
        Log.d("TimeTracking", "Started tracking for " + currentApp);
        Toast.makeText(this, "Started tracking " + currentApp, Toast.LENGTH_SHORT).show();

        // Begin the continuous checking using the handler
        handler.post(continuousCheckRunnable);
    }

    // Stop tracking and save the tracked time data
    private void stopTracking() {
        if (isTracking) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            totalTime = elapsedTime;
            isTracking = false;

            handler.removeCallbacks(continuousCheckRunnable);

            // Save the tracked data if meaningful
            if (totalTime > 0) {
                saveTrackedData(currentApp, totalTime);
                Log.d("TimeTracking", "Stopped tracking for " + currentApp + ". Duration: " + totalTime + " ms");
                Toast.makeText(this, "Stopped tracking " + currentApp, Toast.LENGTH_SHORT).show();
                totalTime = 0;
            }

        }
    }

    // Runnable for continuous checking to ensure reels are being tracked properly
    private final Runnable continuousCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTracking) {
                AccessibilityNodeInfo root = getRootInActiveWindow();
                if (root != null) {
                    checkAndTrackContent(root, currentApp);
                    root.recycle();
                }
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        }
    };

    // Save the tracked time data to the database
    private void saveTrackedData(String appName, long duration) {
        TimeTracking timeTracking = new TimeTracking(
                0,  // ID is auto-generated
                appName,
                duration,
                System.currentTimeMillis(),
                0  // Placeholder value for timeLimit, adjust as needed
        );

        viewModel.insertTimeTracking(timeTracking);
        Log.d("TimeTracking", "Saved tracking data: " + timeTracking.toString());
    }

    @Override
    public void onInterrupt() {
        stopTracking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTracking();
        viewModelStore.clear();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopTracking();
    }




    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }
}
