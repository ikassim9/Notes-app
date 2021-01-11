package com.ismail.mynotes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.settings.*

class SettingActivity : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var appSettingsPref: SharedPreferences
    private lateinit var sharePrefEdit: SharedPreferences.Editor
    private lateinit var SharePrefEditIndex: SharedPreferences.Editor
    private lateinit var chosenThemePref: SharedPreferences
    private var selectedIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        toolbar = findViewById(R.id.settingToolBar)
        supportActionBar?.title = "Settings"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        chosenThemePref = getSharedPreferences("index", Context.MODE_PRIVATE)
        SharePrefEditIndex = chosenThemePref.edit()
        appSettingsPref = getSharedPreferences("appSettingsPref", 0)
        val getPrefTheme = chosenThemePref.getInt("index", -1)

        when(getPrefTheme){
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
                2 -> {
                     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                }
        }

        changeThemes()
    }

    private fun changeThemes() {

        val theme = findViewById<TextView>(R.id.nightModeTxt)
        val themeChoices = arrayOf("Light", "Dark", "System setting")
        theme.setOnClickListener {
            val getPref = chosenThemePref.getInt("index", -1)
            Log.d("getIndex" , "$getPref")

            selectedIndex = getPref

            val dialog = MaterialAlertDialogBuilder(this, R.style.DialogTheme)
            dialog.setTitle("pick a theme")
                .setSingleChoiceItems(themeChoices, selectedIndex) { dialog, which ->
                    selectedIndex = which

                }.setNeutralButton("cancel") { dialog, which -> }

                .setPositiveButton("yes") { dialog, which ->

                    Log.d("index",  "$selectedIndex")
                    when (selectedIndex) {
                        0 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        1 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                       2 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                    }
                    SharePrefEditIndex.putInt("index", selectedIndex)
                    SharePrefEditIndex.apply()
                    Log.d("selected_index", "$selectedIndex")
                }

            dialog.show()
        }
    }
}
