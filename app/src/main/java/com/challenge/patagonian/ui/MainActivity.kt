package com.challenge.patagonian.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.challenge.patagonian.R
import com.challenge.patagonian.data.SharedPreferencesHelper
import com.challenge.patagonian.databinding.ActivityMainBinding
import com.challenge.patagonian.domain.DeviceRotationEvent
import com.challenge.patagonian.domain.SessionCountEvent
import com.challenge.patagonian.utils.size

/**
 * This is the main activity and it contains:
 * @property binding this variable of type ActivityMainBinding is a reference to the activity view
 * binding which is used to control the ui elements
 *
 * @property sharedPreferencesHelper is an instance of the SharedPreferenceHelper class
 *
 * The activity implements two listener interfaces:
 * @see DeviceRotationEvent and it includes two methods
 * onRotatedToLeft is invoked when the conditions are met (see the documentation in GyroscopeListener)
 * onRotatedToRight is invoked when the conditions are met (see the documentation in GyroscopeListener)
 *
 * @see SessionCountEvent it's method onSessionCountIncreased is invoked when the session counter
 * is updated by the SessionManager
 */

class MainActivity : AppCompatActivity(), DeviceRotationEvent, SessionCountEvent {

  private lateinit var binding: ActivityMainBinding
  private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    initSharedPrefsHelper()
    setSessionCounter()
    // init the GyroscopeListeners
    GyroscopeListeners(this, this)
    // init the SessionManager
    SessionManager(this, this, sharedPreferencesHelper)
  }

  private fun initSharedPrefsHelper() {
    sharedPreferencesHelper =
      SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(applicationContext))
  }

  /**
   * this function is only used to update the session counter text view on the screen
   */
  private fun setSessionCounter() {
    binding.tvSessionCounter.text =
      getString(R.string.session_count, sharedPreferencesHelper.sessionsCount.toString())
  }

  override fun onRotatedToLeft() {
    binding.tvSessionCounter.size(12f)
  }

  override fun onRotatedToRight() {
    binding.tvSessionCounter.size(20f)
  }

  override fun onSessionCountIncreased() {
    setSessionCounter()
  }
}