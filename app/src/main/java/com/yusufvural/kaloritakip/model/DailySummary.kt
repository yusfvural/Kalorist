package com.yusufvural.kaloritakip.model

data class DailySummary(
    val totalCalories: Int = 0,
    val goalCalories: Int = 2200,
    val proteinConsumed: Double = 0.0,
    val proteinGoal: Double = 150.0,
    val carbsConsumed: Double = 0.0,
    val carbsGoal: Double = 300.0,
    val fatConsumed: Double = 0.0,
    val fatGoal: Double = 70.0
) {
    val caloriesLeft: Int get() = (goalCalories - totalCalories).coerceAtLeast(0)
    val proteinProgress: Float get() = (proteinConsumed / proteinGoal).toFloat().coerceIn(0f, 1f)
    val carbsProgress: Float get() = (carbsConsumed / carbsGoal).toFloat().coerceIn(0f, 1f)
    val fatProgress: Float get() = (fatConsumed / fatGoal).toFloat().coerceIn(0f, 1f)
}
