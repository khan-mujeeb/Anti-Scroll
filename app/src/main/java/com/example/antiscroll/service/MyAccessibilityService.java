package com.example.antiscroll.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class MyAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo nodeInfo = accessibilityEvent.getSource();

        if (
                accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                || accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED
        ) {
            if (nodeInfo != null) {

                detectAndBlockContent(nodeInfo);
            } else {
                Toast.makeText(this, "Node is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void detectAndBlockContent(AccessibilityNodeInfo node) {
        if (node == null) return;

        Toast.makeText(this, "Detecting content", Toast.LENGTH_SHORT).show();

        // Extract node information
        String className = node.getClassName() != null ? node.getClassName().toString() : "Unknown";
        String text = node.getText() != null ? node.getText().toString() : "No text";
        String contentDescription = node.getContentDescription() != null ? node.getContentDescription().toString() : "No content description";

        // Log the extracted information
        Log.d("ViewHierarchy", "Class: " + className + ", Text: " + text + ", ContentDesc: " + contentDescription  + " child " +   node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_video_container") );



        if (
                        !node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_video_container").isEmpty() &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_like") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_comment") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_share") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_more") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_audio") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_follow") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_reels") != null &&
                        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/row_feed_button_profile") != null

        ) {

            Toast.makeText(this, "Reel layout detected", Toast.LENGTH_SHORT).show();
            // Block the content
            blockContent(node);
            return;
        } else {
            Toast.makeText(this, "Reel layout not detected", Toast.LENGTH_SHORT).show();
        }


        // Recursively check child nodes
        for (int i = 0; i < node.getChildCount(); i++) {
            detectAndBlockContent(node.getChild(i));
        }
    }



    private void blockContent(AccessibilityNodeInfo node) {
        // Perform actions to block the content

        Toast.makeText(this, "Blocking content", Toast.LENGTH_SHORT).show();

        performGlobalAction(GLOBAL_ACTION_BACK);

    }


    @Override
    public void onInterrupt() {

    }
}
