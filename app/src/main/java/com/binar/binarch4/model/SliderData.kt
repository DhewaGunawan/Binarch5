package com.binar.binarch4.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SliderData(
    val title: String,
    val desc: String,
    val imgSlider: String
) : Parcelable
