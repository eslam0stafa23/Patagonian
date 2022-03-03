package com.challenge.patagonian.data

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * this is a helper class that is used to ease the dealing with the shared preferences of the app
 * @property sessionsCount the total count of the sessions logged on the app
 * @property backgroundStartTime the time the app went to the background
 * @property backgroundEndTime the time the app came back from the background
 */
class SharedPreferencesHelper(private val sharedPreferences: SharedPreferences) {

  companion object {
    const val SESSION_COUNT = "SESSION_COUNT"
    const val BACKGROUND_START_TIME = "BACKGROUND_START_TIME"
    const val BACKGROUND_END_TIME = "BACKGROUND_END_TIME"
  }

  var sessionsCount: Int
    get() = sharedPreferences.getInt(SESSION_COUNT, 1)
    set(value) = sharedPreferences.edit { putInt(SESSION_COUNT, value) }

  var backgroundStartTime: Long
    get() = sharedPreferences.getLong(BACKGROUND_START_TIME, 0)
    set(value) = sharedPreferences.edit { putLong(BACKGROUND_START_TIME, value) }

  var backgroundEndTime: Long
    get() = sharedPreferences.getLong(BACKGROUND_END_TIME, 0)
    set(value) = sharedPreferences.edit { putLong(BACKGROUND_END_TIME, value) }

  fun clearTrackingTimes(){
    backgroundEndTime = 0
    backgroundStartTime = 0
  }

}