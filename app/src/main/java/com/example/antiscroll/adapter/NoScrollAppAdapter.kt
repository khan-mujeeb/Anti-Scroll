package com.example.antiscroll.adapter
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.antiscroll.R
import com.example.antiscroll.data.AvailableAppSetting
import com.example.antiscroll.uiUtils.SystemUtils
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.launch

class NoScrollAppAdapter(
    private val context: Context,
    private val appList: List<String>,
    private val availableAppViewModel: AvailableAppSettingViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val availableAppList: List<AvailableAppSetting>

) : RecyclerView.Adapter<NoScrollAppAdapter.NoScrollAppViewHolder>() {




    //   ***** inner class to hold the view *****
    inner class NoScrollAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIconImageView: ImageView = view.findViewById(R.id.appIconImageView)
        val appNameTextView: TextView = view.findViewById(R.id.NoScrollappNameTextView)
        val setTimeLimit: ImageButton = view.findViewById(R.id.setDurationButton)
        val scrollSwitch: SwitchMaterial = view.findViewById(R.id.scrollSwitch)
        val appTypeTextView: TextView = view.findViewById(R.id.appTypeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoScrollAppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.block_scroll_app_itemview, parent, false)
        return NoScrollAppViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: NoScrollAppViewHolder, position: Int) {
        val packageName = appList[position]



        subscribeOnClickEvents(holder, packageName)
        setData(holder, packageName)
        subscribeUI(holder, packageName)
    }

    //
    private fun subscribeUI(holder: NoScrollAppAdapter.NoScrollAppViewHolder, packageName: String) {



        val appSetting = availableAppList.find { it.appName == packageName }
        holder.scrollSwitch.isChecked = appSetting?.isAntiScrollEnabled ?: false



//         1. Observe the availableAppSetting for the app and update the switch enabled or disabled
//        lifecycleOwner.lifecycleScope.launch {
//            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                availableAppViewModel.getAvailableAppSetting(packageName).collect { availableAppSetting ->
//                    holder.scrollSwitch.isChecked = availableAppSetting?.isAntiScrollEnabled ?: false
//                }
//            }
//        }



    }

    private fun subscribeOnClickEvents(holder: NoScrollAppViewHolder, packageName: String) {

        // 1. Set the time limit for the app
        holder.setTimeLimit.setOnClickListener {

            val defaultTime = availableAppList.find { it.appName == packageName }?.upperTimeLimit ?: 0L

            SystemUtils.showTimePickerDialog(context, defaultTime ) { hours, minutes ->
                // Handle the time picked (hours and minutes)
                val totalMinutes = hours * 60 + minutes
                val totalMilliseconds = (totalMinutes * 60 * 1000).toLong()

                lifecycleOwner.lifecycleScope.launch {
                    availableAppViewModel.updateTimeLimit(packageName, totalMilliseconds)
                }
            }
        }

        // 2. Update the app setting when the switch is toggled
        holder.scrollSwitch.setOnCheckedChangeListener { _, isChecked ->

            // Update the app setting in the database
            lifecycleOwner.lifecycleScope.launch {
                availableAppViewModel.updateAppSetting(
                    packageName = packageName,
                    isBlocked = isChecked
                )
            }
        }
    }


    // 2. Set the app name and icon

    private fun setData(holder: NoScrollAppAdapter.NoScrollAppViewHolder, packageName: String) {

        // Get the app name and icon using the package name
        try {
            val packageManager: PackageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName = packageManager.getApplicationLabel(appInfo).toString()
            val appIcon = packageManager.getApplicationIcon(packageName)

            holder.appNameTextView.text = appName
            holder.appIconImageView.setImageDrawable(appIcon)


        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("AppInfo", "Package not found: $packageName", e)

            holder.itemView.visibility = View.GONE // Hide the item if the app is not found
        }
    }
}

