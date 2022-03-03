package com.challenge.patagonian.utils

import android.util.TypedValue
import android.widget.TextView

/**
 * an extension function used to set a textView's size in sp
 * @receiver the textView that the size will be set on
 * @param value is the value to be set on the textView
 */
fun TextView.size(value: Float) {
  this.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
}
