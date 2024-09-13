package com.example.antiscroll.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.antiscroll.data.TimeTracking;
import com.example.antiscroll.db.TimeTrackingDatabase;
import com.example.antiscroll.repository.TimeTrackingRepository;
import com.example.antiscroll.viewmodel.TimeTrackingViewModel;
import com.example.antiscroll.viewmodel.TimeTrackingViewModelFactory;

public class MyAccessibilityService extends AccessibilityService implements ViewModelStoreOwner {
    private final ViewModelStore viewModelStore = new ViewModelStore();
    private long startTime = 0;
    private long totalTimeSpent = 0;
    private boolean isTracking = false;
    private String currentApp = "";
    private TimeTrackingViewModel viewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the ViewModel using the factory

        TimeTrackingRepository repository = new TimeTrackingRepository(TimeTrackingDatabase.Companion.getDataBase(this).timeTrackingDao());
        TimeTrackingViewModelFactory factory = new TimeTrackingViewModelFactory(repository);
        viewModel = new ViewModelProvider(viewModelStore, factory).get(TimeTrackingViewModel.class);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo nodeInfo = accessibilityEvent.getSource();

        if (
                accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                        || accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED
        ) {

            // Block the content if the total time spent exceeds 1 minute


            if (nodeInfo != null) {

                detectAndBlockContent(nodeInfo);
            } else {
                Toast.makeText(this, "Node is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void detectAndBlockContent(AccessibilityNodeInfo node) {

        if(totalTimeSpent > 60000) blockContent();
        Log.d("total time spent", String.valueOf(totalTimeSpent) + " status " + isTracking);
        if (node == null) return;

        Toast.makeText(this, "Detecting content", Toast.LENGTH_SHORT).show();

        // Extract node information
        String className = node.getClassName() != null ? node.getClassName().toString() : "Unknown";
        String text = node.getText() != null ? node.getText().toString() : "No text";
        String contentDescription = node.getContentDescription() != null ? node.getContentDescription().toString() : "No content description";

        // Log the extracted information
//        Log.d("ViewHierarchy", "Class: " + className + ", Text: " + text + ", ContentDesc: " + contentDescription  + " child " +   node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_video_container") );


        if (
                !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_video_container").isEmpty()

        ) {


            Toast.makeText(this, "Reel layout detected", Toast.LENGTH_SHORT).show();

            if (!isTracking) {
                startTracking();
            }


            // Block the content
//            blockContent(node);
            return;
        } else {
            stopTracking();
            Toast.makeText(this, "Reel layout not detected", Toast.LENGTH_SHORT).show();
        }


        // Recursively check child nodes
        for (int i = 0; i < node.getChildCount(); i++) {
            detectAndBlockContent(node.getChild(i));
        }
    }


    private void blockContent() {
        // Perform actions to block the content

        Toast.makeText(this, "Blocking content", Toast.LENGTH_SHORT).show();

        performGlobalAction(GLOBAL_ACTION_BACK);

    }

    private void startTracking() {
        startTime = System.currentTimeMillis();
        isTracking = true;
        // Identify the current app, here using a placeholder for demonstration
        currentApp = "Instagram"; // You might want to extract this dynamically based on the event
    }

    private void stopTracking() {
        if (isTracking) {
            totalTimeSpent = System.currentTimeMillis() - startTime;
            isTracking = false;


            // Save tracked time into the database
            saveTrackedData(currentApp, totalTimeSpent);
        }
    }

    private void saveTrackedData(String appName, long duration) {
        // Create a new TimeTracking object
        TimeTracking timeTracking = new TimeTracking(
                0,  // id is auto-generated
                appName,
                duration,
                System.currentTimeMillis(),
                0  // Placeholder value for timeLimit, adjust as needed
        );
        Log.d("TimeTracking", timeTracking.toString());
        Toast.makeText(this, "Saving tracking data", Toast.LENGTH_SHORT).show();
        // Insert the tracking data into the database using the ViewModel
        viewModel.insertTimeTracking(timeTracking);
    }

    @Override
    public void onInterrupt() {
        stopTracking(); // Ensure tracking stops when the service is interrupted
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTracking(); // Ensure tracking stops when the service is destroyed
        viewModelStore.clear(); // Clear the ViewModelStore
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }


}
