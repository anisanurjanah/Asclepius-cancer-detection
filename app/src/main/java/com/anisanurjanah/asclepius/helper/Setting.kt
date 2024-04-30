package com.anisanurjanah.asclepius.helper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Setting(
    val menu: String
) : Parcelable