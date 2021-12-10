package ru.viksimurg.viknote.repository.shprefs

import android.content.Context
import ru.viksimurg.viknote.utils.PREFS_NAME

class ShPrefsDataSource(val context: Context): IShPrefs {

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getInt(key: String, default: Int): Int {
        return sharedPrefs.getInt(key, default) ?: default
    }

    override fun saveInt(key: String, value: Int) {
        sharedPrefs.edit().putInt(key, value).apply()
    }

    override fun getString(key: String, default: String): String {
        return sharedPrefs.getString(key, default) ?: default
    }

    override fun saveString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

}