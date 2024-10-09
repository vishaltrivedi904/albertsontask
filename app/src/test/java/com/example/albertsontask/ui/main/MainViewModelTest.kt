package com.example.albertsontask.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.albertsontask.data.api.NetworkResponse
import com.example.albertsontask.data.model.common.ErrorBody
import com.example.albertsontask.data.model.user.Coordinates
import com.example.albertsontask.data.model.user.Location
import com.example.albertsontask.data.model.user.Name
import com.example.albertsontask.data.model.user.Picture
import com.example.albertsontask.data.model.user.Result
import com.example.albertsontask.data.model.user.Street
import com.example.albertsontask.data.model.user.Timezone
import com.example.albertsontask.data.model.user.UserModel
import com.example.albertsontask.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class MainViewModelTest {

    @Mock
    lateinit var repository: Repository

    private val testDispatcher= StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_getUsers() = runTest {
        val results = mutableListOf<Result>()
        val user = Result(
            gender = "male",
            email = "abc@example.com",
            phone = "1234567891",
            location = Location(
                city = "New York",
                coordinates = Coordinates(latitude = "40.7128", longitude = "74.0060"),
                country = "US",
                postcode = "73001",
                state = "",
                street = Street(name = "ABC", number = 1),
                timezone = Timezone(description = "US", offset = "+1")
            ),
            name = Name(first = "Jhon", last = "Smith", title = "Mr"),
            picture = Picture(
                large = "http://www.pixel.com/abc.jpg",
                medium = "http://www.pixel.com/abc.jpg",
                thumbnail = "http://www.pixel.com/abc.jpg"
            )
        )

        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)
        results.add(user)

        val userModel = UserModel(results)

        Mockito. `when` (repository.getUsers(1,10,"name,location,picture,gender,email,phone")).thenReturn(NetworkResponse.Success(userModel))
        val sut=MainViewModel(repository)
        sut.getUsers(1,10,"name,location,picture,gender,email,phone")
        testDispatcher.scheduler.advanceUntilIdle()
        val result=sut.data.getOrAwaitValue()
        Assert.assertEquals(userModel,result.data)
    }

    @Test
    fun test_getUsersError() = runTest {
        val errorMessage = "Uh oh, something has gone wrong. Please tweet us @randomapi about the issue. Thank you."
        val errorBody = ErrorBody(errorMessage)

        `when`(repository.getUsers(1, 10, "name,location,picture,gender,email,phone"))
            .thenReturn(NetworkResponse.Error(errorBody.error, null, 400))

        val sut = MainViewModel(repository)

        sut.getUsers(1, 10, "name,location,picture,gender,email,phone")

        testDispatcher.scheduler.advanceUntilIdle()

        // Get the result from LiveData
        val result = sut.data.getOrAwaitValue()


        Assert.assertTrue(result is NetworkResponse.Error)
        val errorResult = result as NetworkResponse.Error
        Assert.assertEquals(errorBody.error, errorResult.message)
        Assert.assertEquals(400, errorResult.code)
    }

    @Test
    fun test_getUsersFailure() = runTest {
        val throwable = Throwable("Network Error Occurred")

        `when`(repository.getUsers(1, 10, "name,location,picture,gender,email,phone"))
            .thenReturn(NetworkResponse.Failure("Exception", null, throwable))

        val sut = MainViewModel(repository)
        sut.getUsers(1, 10, "name,location,picture,gender,email,phone")

        testDispatcher.scheduler.advanceUntilIdle()

        val result = sut.data.getOrAwaitValue()

        Assert.assertTrue(result is NetworkResponse.Failure)
        val failureResult = result as NetworkResponse.Failure
        Assert.assertEquals(throwable, failureResult.throwable)
    }



    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

private fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
