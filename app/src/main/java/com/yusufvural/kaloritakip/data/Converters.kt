package com.yusufvural.kaloritakip.data

import androidx.room.TypeConverter
import com.yusufvural.kaloritakip.model.MealType

class Converters {
    @TypeConverter
    fun fromMealType(value: MealType): String {
        return value.name
    }

    @TypeConverter
    fun toMealType(value: String): MealType {
        return MealType.valueOf(value)
    }
}
