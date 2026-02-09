package com.yusufvural.kaloritakip.ui.profile

import com.yusufvural.kaloritakip.MainDispatcherRule
import com.yusufvural.kaloritakip.domain.UserRepository
import com.yusufvural.kaloritakip.domain.repository.AuthRepository
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
import org.mockito.kotlin.argumentCaptor
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        authRepository = mock(AuthRepository::class.java)

        `when`(userRepository.getUser()).thenReturn(flowOf(UserEntity(id = "test_user", name = "Test User")))
        
        viewModel = ProfileViewModel(userRepository, authRepository)
    }

    @Test
    fun `saveUser updates repository`() = runTest {
        val updatedUser = UserEntity(id = "test_user", currentWeight = 80.0)
        viewModel.saveUser(updatedUser)
        
        advanceUntilIdle()

        verify(userRepository).saveUser(updatedUser)
    }

    @Test
    fun `logout calls auth repository`() = runTest {
        viewModel.logout()
        verify(authRepository).logout()
    }
}
