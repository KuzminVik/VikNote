package ru.viksimurg.viknote.repository.shprefs

interface IShPrefs {
    fun getInt(key: String, default: Int): Int
    fun saveInt(key: String, value: Int)
    fun getString(key: String, default: String): String
    fun saveString(key: String, value: String)
}