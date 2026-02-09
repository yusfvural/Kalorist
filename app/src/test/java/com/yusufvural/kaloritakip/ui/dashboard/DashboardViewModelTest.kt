package com.yusufvural.kaloritakip.ui.dashboard

import com.yusufvural.kaloritakip.MainDispatcherRule
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.domain.UserRepository
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: DashboardViewModel
    private lateinit var foodRepository: FoodRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        foodRepository = mock(FoodRepository::class.java)
        userRepository = mock(UserRepository::class.java)

        // Default mock behaviors
        `when`(userRepository.getUser()).thenReturn(flowOf(UserEntity(id = "test_user")))
        `when`(foodRepository.getEntriesForDay(any())).thenReturn(flowOf(emptyList()))
        `when`(foodRepository.getExercisesForDay(any())).thenReturn(flowOf(emptyList()))

        viewModel = DashboardViewModel(foodRepository, userRepository)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(0, state.summary.totalCalories)
        assertEquals(0, state.entries.size)
    }

    @Test
    fun `addFood calls repository`() = runTest {
        viewModel.addFood(
            name = "Elma",
            calories = 100,
            protein = 0.5,
            carbs = 25.0,
            fat = 0.2,
            mealType = com.yusufvural.kaloritakip.model.MealType.SNACK
        )

        verify(foodRepository).addFoodEntry(any())
    }
    
    // Note: Testing Flow collection in init block is tricky without creating proper fakes or using Turbine.
    // For this quick test setup, we are verifying that the repository interaction happens.
}
