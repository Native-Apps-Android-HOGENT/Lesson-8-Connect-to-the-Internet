package com.example.android.marsrealestate.overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.marsrealestate.network.MarsApiService
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OverviewViewModelTest {

    private lateinit var viewModel: OverviewViewModel

    private val marsApiService = mockk<MarsApiService>()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantTestExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = OverviewViewModel(marsApiService)
    }

    @Test
    fun overviewViewModel_workingAPI_responseGetsUpdated() {
        // Arrange
        every { marsApiService.getProperties() } returns CompletableDeferred(listOf(mockk()))

        runBlockingTest {
            // Act
            viewModel = OverviewViewModel(marsApiService)

        }
        // Assert
        assertNotNull(viewModel.properties.getValueForTest())

        assertNotNull(viewModel.status.getValueForTest())
        assertEquals(MarsApiStatus.DONE, viewModel.status.getValueForTest()!!)
    }

    @Test
    fun overviewViewModel_ApiError_responseIndicatesFailure() {
        // Arrange
        every { marsApiService.getProperties() } throws RuntimeException()

        runBlockingTest {
            // Act
            viewModel = OverviewViewModel(marsApiService)

        }
        // Assert
        assertTrue(viewModel.properties.getValueForTest()!!.isEmpty())

        assertNotNull(viewModel.status.getValueForTest())
        assertEquals(MarsApiStatus.ERROR, viewModel.status.getValueForTest()!!)
    }

    @After
    fun destroy() {
        clearMocks(marsApiService)
    }

}