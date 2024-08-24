package com.example.antiscroll;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class MyAccessibilityService extends AccessibilityService {



    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo nodeInfo = accessibilityEvent.getSource();

        if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            if(nodeInfo != null){
                detectAndBlockContent(nodeInfo);
            }
            else {
                Toast.makeText(this, "Node is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void detectAndBlockContent(AccessibilityNodeInfo node) {
        if (node == null) return;

        Toast.makeText(this, "Detecting content", Toast.LENGTH_SHORT).show();

        // Check if the node matches the criteria
        if (isReelLayout(node)) {

            Toast.makeText(this, "Reel layout detected", Toast.LENGTH_SHORT).show();
            // Block the content
            blockContent(node);
        } else {
            Toast.makeText(this, "Reel layout not detected", Toast.LENGTH_SHORT).show();
        }

        // Recursively check child nodes
        for (int i = 0; i < node.getChildCount(); i++) {
            detectAndBlockContent(node.getChild(i));
        }
    }

    private boolean isReelLayout(AccessibilityNodeInfo node) {
        // Example criteria for detecting a reel layout
        if ("androidx.recyclerview.widget.RecyclerView".equals(node.getClassName())) {
            // Further checks, like checking the child nodes
            return true; // Return true if this is a reel layout
        }
        return false;
    }


    private void blockContent(AccessibilityNodeInfo node) {
        // Perform actions to block the content

        Toast.makeText(this, "Blocking content", Toast.LENGTH_SHORT).show();
//        performGlobalAction(GLOBAL_ACTION_BACK); // Simulate back press
    }




    @Override
    public void onInterrupt() {

    }
}
