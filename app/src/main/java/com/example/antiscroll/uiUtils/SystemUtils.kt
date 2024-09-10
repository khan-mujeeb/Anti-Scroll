package com.example.antiscroll.uiUtils

import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.TimePicker

object SystemUtils {


    //   ***** function to show the time picker dialog *****
    fun showTimePickerDialog(context: Context) {
        // Get the current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                // Handle the selected time
                onTimeSelected(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true // Set to false for 12-hour format, true for 24-hour format
        )

        // Show the TimePickerDialog
        timePickerDialog.show()
    }

    private fun onTimeSelected(hour: Int, minute: Int) {
        // Do something with the selected time
        // For example, you can show it in a Toast or TextView
        // Toast.makeText(this, "Selected Time: $hour:$minute", Toast.LENGTH_SHORT).show()
        println("Selected Time: $hour:$minute")
    }
}