package com.yusufvural.kaloritakip.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yusufvural.kaloritakip.data.api.FoodApiService
import com.yusufvural.kaloritakip.data.api.FoodSearchResponse
import com.yusufvural.kaloritakip.data.api.Product
import com.yusufvural.kaloritakip.data.api.Nutriments
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.MealType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

@ExperimentalCoroutinesApi
class FoodRepositoryImplTest {

    private lateinit var repository: FoodRepositoryImpl
    private lateinit var foodDao: FoodDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var waterDao: WaterDao
    private lateinit var apiService: FoodApiService
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setup() {
        foodDao = mock(FoodDao::class.java)
        exerciseDao = mock(ExerciseDao::class.java)
        waterDao = mock(WaterDao::class.java)
        apiService = mock(FoodApiService::class.java)
        firebaseAuth = mock(FirebaseAuth::class.java)
        firebaseUser = mock(FirebaseUser::class.java)

        `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn("test_user_id")

        repository = FoodRepositoryImpl(foodDao, exerciseDao, waterDao, apiService, firebaseAuth)
    }

    @Test
    fun `addFoodEntry attaches current userId`() = runTest {
        val entry = FoodEntry(
            id = "1",
            name = "Test Food",
            calories = 100,
            protein = 10.0,
            carbs = 10.0,
            fat = 5.0,
            mealType = MealType.SNACK,
            userId = "" // Empty initially
        )

        repository.addFoodEntry(entry)

        val captor = argumentCaptor<FoodEntry>()
        verify(foodDao).insertEntry(captor.capture())

        val capturedEntry = captor.firstValue
        assertEquals("test_user_id", capturedEntry.userId)
    }

    @Test
    fun `searchFood returns mapped results`() = runTest {
        val mockResponse = FoodSearchResponse(
            products = listOf(
                Product(
                    product_name = "Test Product",
                    nutriments = Nutriments(
                        `energy-kcal_100g` = 100.0,
                        proteins_100g = 10.0,
                        fat_100g = 5.0,
                        carbohydrates_100g = 20.0
                    )
                )
            )
        )

        `when`(apiService.searchFood(any(), any(), any(), any(), any())).thenReturn(mockResponse)

        val result = repository.searchFood("query")
        
        assertEquals(true, result.isSuccess)
        val searchResults = result.getOrNull()!!
        assertEquals(1, searchResults.size)
        assertEquals("Test Product", searchResults[0].label)
        assertEquals(100, searchResults[0].calories)
    }
}
