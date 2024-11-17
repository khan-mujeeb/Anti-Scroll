package com.example.antiscroll.uiUtils

import android.app.ActivityManager
import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.antiscroll.R
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object SystemUtils {



    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    fun getForegroundApp(context: Context): String {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfo = activityManager.getRunningTasks(1)
        if (runningTaskInfo.isEmpty()) {
            return ""
        }
        val componentInfo = runningTaskInfo[0].topActivity
        return componentInfo!!.packageName
    }

    //   *********** function to show the time picker dialog ****************
    fun showTimePickerDialog(context: Context, defaultTime: Long,  onTimeSet: (hours: Int, minutes: Int) -> Unit) {
        // Inflate your custom view with a layout that contains two NumberPickers for hours and minutes
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null)
        val hoursPicker = dialogView.findViewById<NumberPicker>(R.id.hoursPicker)
        val minutesPicker = dialogView.findViewById<NumberPicker>(R.id.minutesPicker)

        // Set the min and max values for the pickers
        hoursPicker.minValue = 0
        hoursPicker.maxValue = 7
        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59

        // Create arrays with "hrs" and "mins" postfix
        val hoursDisplayedValues = Array(24) { "$it hrs" }
        val minutesDisplayedValues = Array(60) { "$it mins" }

        // Set displayed values
        hoursPicker.displayedValues = hoursDisplayedValues
        minutesPicker.displayedValues = minutesDisplayedValues

        // miliseconds to hours and minutes
        val totalMinutes = defaultTime / 1000 / 60
        val hours = (totalMinutes / 60).toInt()
        val minutes = (totalMinutes % 60).toInt()

        // Set default values (20 minutes = 0 hours and 20 minutes)
        hoursPicker.value = hours
        minutesPicker.value = minutes

        // Build the dialog
        val dialog = AlertDialog.Builder(context)
            .setTitle("Set app limit")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                // Get the selected hours and minutes (without postfix)
                val selectedHours = hoursPicker.value
                val selectedMinutes = minutesPicker.value
                onTimeSet(selectedHours, selectedMinutes)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }



    //  *********** function to format the time in HH:MM format ****************
    fun formatMillisToHHMM(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

//        return when {
//            hours == 0L && minutes == 0L -> "$seconds seconds"
//            hours == 0L -> "$minutes minutes"
//            minutes == 0L -> "$hours hour"
//            hours == 0L && minutes == 0L && seconds == 0L -> "No data"
//            else -> "$hours hours $minutes minutes"
//        }

        return "$hours:$minutes:$seconds"
    }
}