package com.penchan.noodoeapi.util

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    val PREF_NAME = "user_data"
    val USER_NAME = "USERNAME"
    val TIME_ZONE = "TIMEZONE"
    val USER_MAIL = "MAIL"
    val OBJECT_ID = "OBJECTID"
    val TOKEN = "TOKEN"


    fun getPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.add(operation: (SharedPreferences.Editor) -> Unit) {
        val add = edit()
        operation(add)
        add.apply()
    }

    var SharedPreferences.mail
        get() = getString(USER_MAIL, "")
        set(value) {
            add {
                it.putString(USER_MAIL, value)
            }
        }

    var SharedPreferences.userName
        get() = getString(USER_NAME, "")
        set(value) {
            add {
                it.putString(USER_NAME, value)
            }
        }

    var SharedPreferences.timeZone
        get() = getString(TIME_ZONE, "")
        set(value) {
            add {
                it.putString(TIME_ZONE, value)
            }
        }

    var SharedPreferences.objectId
        get() = getString(OBJECT_ID, "")
        set(value) {
            add {
                it.putString(OBJECT_ID, "")
            }
        }

    var SharedPreferences.token
        get() = getString(TOKEN, "")
        set(value) {
            add {
                it.putString(TOKEN, value)
            }
        }
}