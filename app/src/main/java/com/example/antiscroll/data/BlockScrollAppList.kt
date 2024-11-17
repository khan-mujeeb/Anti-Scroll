package com.example.antiscroll.data

object BlockScrollAppList {
    val appList = listOf(
        "com.instagram.android",  // Instagram
        "com.google.android.youtube",  // YouTube
        // "com.twitter.android"  // Twitter/X (uncomment if needed)
    )

    val defaultSettings = listOf(
        AvailableAppSetting(appName = appList[0], upperTimeLimit = 1200000, isAntiScrollEnabled = true),
        AvailableAppSetting(appName = appList[1], upperTimeLimit = 1200000, isAntiScrollEnabled = true)
    )

}
