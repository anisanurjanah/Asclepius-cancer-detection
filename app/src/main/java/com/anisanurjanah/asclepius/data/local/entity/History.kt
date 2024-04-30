package com.anisanurjanah.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity(tableName = "history")
@Parcelize
data class History(
    @PrimaryKey(autoGenerate = false)

    @field:ColumnInfo(name = "label")
    var label: String = "",

    @field:ColumnInfo(name = "imageUri")
    var imageUri: String? = null,

    @field:ColumnInfo(name = "score")
    var score: String? = null,

    @field:ColumnInfo(name = "timestamp")
    var timestamp: String? = null
) : Parcelable