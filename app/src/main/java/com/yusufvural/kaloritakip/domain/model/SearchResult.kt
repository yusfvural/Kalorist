package com.yusufvural.kaloritakip.domain.model

data class SearchResult(
    val label: String,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)
