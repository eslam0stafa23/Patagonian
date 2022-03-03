package com.challenge.patagonian.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.challenge.patagonian.domain.DeviceRotationEvent

/**
 * This class is a lifecycle aware class and it's bound to the main activity lifecycle
 * it's main responsibility is to handle and manage the gyroscope events and it contains the business
 * logic related to the rotation conditions requested in the requirements
 *
 * @property deviceRotationEvent contains the listeners that is used to signal events to the main activity
 * @property currentDevicePositionOnZAxis of type Double, it's used to keep track of the current device position
 * in rotation. It's initial value is 0
 * @property sensorManager SensorManager
 * @property gyroscopeSensor Sensor
 */
class GyroscopeListeners(
  activity: AppCompatActivity,
  private val deviceRotationEvent: DeviceRotationEvent
) : SensorEventListener, LifecycleEventObserver {

  private val sensorManager: SensorManager
  private val gyroscopeSensor: Sensor
  private var currentDevicePositionOnZAxis = 0.0

  /**
   * init method is used to bound this class to the main activity lifecycle and initiate the
   * required classes: sensorManager and gyroScopeSensor
   */
  init {
    activity.lifecycle.addObserver(this)
    sensorManager = activity.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
  }

  /**
   * This method is used to control the actions will be taken on each lifecycle event
   * in our case we only use two:
   * ON_RESUME to register the sensor event listener to the sensor manager
   * ON_STOP to un-register the sensor event listener from the sensor manager
   */
  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    if (event == Lifecycle.Event.ON_RESUME) {
      register()
    } else if (event == Lifecycle.Event.ON_STOP) {
      unregister()
    }
  }

  /**
   * This method is invoked the sensor has a new event and we use it to keep track of the device's
   * rotation degree around the Z axis and then check if it's more or equal to 30 then we invoke
   * the onRotateToLeft listener and if less or equal to -30 then we invoke the onRotateToRight listener
   */
  override fun onSensorChanged(sensorEvent: SensorEvent) {
    currentDevicePositionOnZAxis += sensorEvent.values[2]
    if (currentDevicePositionOnZAxis >= 30.0f) {
      deviceRotationEvent.onRotatedToLeft()
    } else if (currentDevicePositionOnZAxis <= -30.0f) {
      deviceRotationEvent.onRotatedToRight()

    }
  }

  override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

  private fun register() {
    sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST)
  }

  private fun unregister() {
    sensorManager.unregisterListener(this)
  }
}