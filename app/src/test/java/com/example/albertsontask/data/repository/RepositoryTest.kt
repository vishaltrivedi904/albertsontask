package com.example.albertsontask.data.repository

import com.example.albertsontask.data.api.ApiInterface
import com.example.albertsontask.data.api.NetworkResponse
import com.example.albertsontask.data.model.user.Coordinates
import com.example.albertsontask.data.model.user.Location
import com.example.albertsontask.data.model.user.Name
import com.example.albertsontask.data.model.user.Picture
import com.example.albertsontask.data.model.user.Result
import com.example.albertsontask.data.model.user.Street
import com.example.albertsontask.data.model.user.Timezone
import com.example.albertsontask.data.model.user.UserModel
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

class RepositoryTest {

    @Mock
    lateinit var apiInterface: ApiInterface

    private lateinit var repository: RepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = RepositoryImp(apiInterface)
    }

    @Test
    fun test_getUsers_Success() = runTest {
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

        val response = Response.success(userModel)

        `when`(apiInterface.getUsers(1, 10, "name,location,picture,gender,email,phone"))
            .thenReturn(response)

        val result = repository.getUsers(1, 10, "name,location,picture,gender,email,phone")

        Assert.assertTrue(result is NetworkResponse.Success)
        Assert.assertEquals(10,result.data?.results?.size )
        val successResult = result as NetworkResponse.Success
        Assert.assertEquals(userModel, successResult.data)
    }

    @Test
    fun test_getUsers_Error() = runTest {
        val errorResponse = Response.error<UserModel>(400, ResponseBody.create("application/json".toMediaTypeOrNull(), "{\"error\": \"Bad Request\"}"))

        `when`(apiInterface.getUsers(1, 10, "name,location,picture,gender,email,phone"))
            .thenReturn(errorResponse)

        val result = repository.getUsers(1, 10, "name,location,picture,gender,email,phone")

        Assert.assertTrue(result is NetworkResponse.Error)
        val errorResult = result as NetworkResponse.Error
        Assert.assertEquals("Bad Request", errorResult.message)
        Assert.assertEquals(400, errorResult.code)
    }

    @After
    fun tearDown() {
        // Clean up resources if needed
    }
}