package com.king.superdemo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.king.superdemo.R


class PreferenceFragment : PreferenceFragmentCompat() {

    lateinit var firstPreference: Preference


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.v("wq", "onCreatePreferences: rootkey ="+ rootKey)
        setPreferencesFromResource(R.xml.preferences, rootKey)
        firstPreference = preferenceManager.findPreference<Preference>("first") as Preference
        firstPreference.setOnPreferenceClickListener {
            Log.v("wq", "onCreatePreferences: key= "+ firstPreference.key)
            true}
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        Log.v("wq", "onPreferenceTreeClick: key="+ preference.key)
        return super.onPreferenceTreeClick(preference)
    }
}