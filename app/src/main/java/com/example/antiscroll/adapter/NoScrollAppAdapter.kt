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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.antiscroll.R
import com.example.antiscroll.uiUtils.SystemUtils
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.launch

class NoScrollAppAdapter(
    private val context: Context,
    private val appList: List<String>,
    private val availableAppViewModel: AvailableAppSettingViewModel,
    private val lifecycleOwner: LifecycleOwner

) : RecyclerView.Adapter<NoScrollAppAdapter.NoScrollAppViewHolder>() {



    //   ***** inner class to hold the view *****
    inner class NoScrollAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIconImageView: ImageView = view.findViewById(R.id.appIconImageView)
        val appNameTextView: TextView = view.findViewById(R.id.NoScrollappNameTextView)
        val setTimeLimit: ImageButton = view.findViewById(R.id.setDurationButton)
        val scrollSwitch: SwitchMaterial = view.findViewById(R.id.scrollSwitch)
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

    private fun subscribeUI(holder: NoScrollAppAdapter.NoScrollAppViewHolder, packageName: String) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                availableAppViewModel.getAvailableAppSetting(packageName).collect { availableAppSetting ->
                    holder.scrollSwitch.isChecked = availableAppSetting?.isAntiScrollEnabled ?: false
                }
            }
        }

    }

    private fun subscribeOnClickEvents(holder: NoScrollAppViewHolder, packageName: String) {
        holder.setTimeLimit.setOnClickListener {
            SystemUtils.showTimePickerDialog(context)
        }

        holder.scrollSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleOwner.lifecycleScope.launch {
                availableAppViewModel.updateAppSetting(
                    packageName = packageName,
                    isBlocked = isChecked
                )
            }
        }
    }

    private fun setData(holder: NoScrollAppAdapter.NoScrollAppViewHolder, packageName: String) {

        // Get the app name and icon using the package name
        try {
            val packageManager: PackageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName = packageManager.getApplicationLabel(appInfo).toString()
            val appIcon = packageManager.getApplicationIcon(packageName)

            holder.appNameTextView.text = appName
            holder.appIconImageView.setImageDrawable(appIcon)



            Log.d("AppInfo", "App name: $appName, Package: $packageName")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("AppInfo", "Package not found: $packageName", e)
            holder.appNameTextView.text = "App not found"
            holder.appIconImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}

