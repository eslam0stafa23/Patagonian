package com.challenge.patagonian.domain

interface DeviceRotationEvent {
  fun onRotatedToLeft()
  fun onRotatedToRight()
}