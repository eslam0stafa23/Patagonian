package com.challenge.patagonian.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.challenge.patagonian.data.SharedPreferencesHelper
import com.challenge.patagonian.domain.SessionCountEvent
import java.util.concurrent.TimeUnit

/**
 * This class is a lifecycle aware class and it's bound to the main activity lifecycle
 * It's main responsibility is to handle and manage the session business logic
 *
 * @property sessionCountEvent contains the listener that is used to signal events to the main activity
 * @constructor
 */
class SessionManager(
  activity: AppCompatActivity,
  private val sessionCountEvent: SessionCountEvent,
  private val sharedPreferencesHelper: SharedPreferencesHelper
) : LifecycleEventObserver {

  var isAppComingFromBackground = false
  var isNewSessionStarted = false
  var isSessionLogged = false

  /**
   * init method is used to bound this class to the main activity lifecycle
   */
  init {
    activity.lifecycle.addObserver(this)
  }

  /**
   * This method is used to control the actions will be taken on each lifecycle event
   * in our case we use three methods:
   * ON_START to log that the app came back from the background
   * ON_STOP to log that the app went to the background
   * ON_DESTROY to clear the data we use per session from the shared preferences
   */
  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
      Lifecycle.Event.ON_START -> {
        logBackgroundEventEnd()
      }
      Lifecycle.Event.ON_STOP -> {
        logBackgroundEventStart()
      }
      Lifecycle.Event.ON_DESTROY -> {
        sharedPreferencesHelper.clearTrackingTimes()
      }
      else -> {}
    }
  }

  /**
   * this method is used to log that the app came back from the background after checking that
   * it's coming from the background not the first launch and also check if we already counted
   * a valid session or not yet!
   * and if all the conditions are met we save the time of the log to the shared preferences and
   * then call calculateTimeDifference()
   */
  private fun logBackgroundEventEnd() {
    if (!isSessionLogged) {
      if (isAppComingFromBackground) {
        sharedPreferencesHelper.backgroundEndTime = System.currentTimeMillis()
        calculateTimeDifference()
        isAppComingFromBackground = false
      }
    }
  }

  /**
   * this method is used to log that the app is going the background after checking that
   * a new session is not started yet to prevent setting the start time each time the app goes to
   * the background, and also check if we already counted a valid session or not yet!
   * and if all the conditions are met we save the time of the log to the shared preferences
   */
  private fun logBackgroundEventStart() {
    if (!isSessionLogged) {
      if (!isNewSessionStarted) {
        sharedPreferencesHelper.backgroundStartTime = System.currentTimeMillis()
        isNewSessionStarted = true
      }
      isAppComingFromBackground = true
    }
  }

  /**
   * this method is used to calculate the difference between the time the app started going to
   * the background and the time the app came back from the background
   * and if the difference is more than 10 minutes (as requested in the requirements) then we increase
   * the session count by 1 and then invoke the onSessionCountIncreased listener
   */
  private fun calculateTimeDifference() {
    val differenceInMill =
      sharedPreferencesHelper.backgroundEndTime.minus(sharedPreferencesHelper.backgroundStartTime)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMill)
    if (minutes >= 10) {
      sharedPreferencesHelper.sessionsCount += 1
      isSessionLogged = true
      sessionCountEvent.onSessionCountIncreased()
    }
  }

}