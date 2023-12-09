package com.example.storyapp.model

import android.content.Context

class UserPreferences (context: Context){
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun saveUser(userModel: UserModel){
        val editor = preferences.edit()
        editor.putString(NAME, userModel.name)
        editor.putString(SESSION, userModel.token)
        editor.putBoolean(STATE_KEY, userModel.isLogin)
        editor.putString(ID, userModel.userid)
        editor.apply()
    }

    fun getUser(): UserModel {
        return UserModel(
            preferences.getString(NAME, "") ?: "",
            preferences.getString(ID, "") ?: "",
            preferences.getBoolean(STATE_KEY, false) ?: false,
            preferences.getString(SESSION, "") ?: ""
        )
    }

    fun logOut(){
        val editor = preferences.edit()
        editor.remove(NAME)
        editor.remove(SESSION)
        editor.remove(STATE_KEY)
        editor.remove(ID)
        editor.apply()
    }

    companion object{
        const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val ID = "id"
        const val SESSION = "session"
        private const val STATE_KEY = "state_key"
    }
}