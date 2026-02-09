package com.yusufvural.kaloritakip.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yusufvural.kaloritakip.model.ExerciseEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise_table WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllExercises(userId: String): Flow<List<ExerciseEntry>>

    @Query("SELECT * FROM exercise_table WHERE userId = :userId AND timestamp >= :dayStart AND timestamp < :dayEnd")
    fun getExercisesForDay(userId: String, dayStart: Long, dayEnd: Long): Flow<List<ExerciseEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntry)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntry)
}
