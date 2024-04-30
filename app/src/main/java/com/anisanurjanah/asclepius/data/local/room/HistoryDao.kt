package com.anisanurjanah.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anisanurjanah.asclepius.data.local.entity.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAllHistory(): LiveData<List<History>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(history: History)
}