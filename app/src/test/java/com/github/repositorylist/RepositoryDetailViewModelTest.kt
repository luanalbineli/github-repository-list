package com.github.repositorylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.repositorylist.common.BaseTest
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.response.UserResponseModel
import com.github.repositorylist.repository.github.IGithubRepository
import com.github.repositorylist.ui.detail.RepositoryDetailViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RepositoryDetailViewModelTest : BaseTest() {
    private val mGithubRepository: IGithubRepository = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `Init test`() {
        val mockedUserResponseModel = faker.newUserResponseModel()
        val mockedRepositoryResponseModel = faker.newRepositoryResponseModel()

        mockUserResponse(mockedUserResponseModel)

        val viewModelInstance = getViewModelInstance()
        viewModelInstance.init(mockedRepositoryResponseModel)

        verify {
            mGithubRepository.getUser(
                viewModelInstance.viewModelScope,
                mockedRepositoryResponseModel.owner.login
            )
        }

        assertEquals(mockedRepositoryResponseModel, viewModelInstance.repositoryResponseModel.value)
        assertEquals(mockedUserResponseModel, viewModelInstance.userResponseModel.value?.data)
    }

    @Test
    fun `Retry user request test`() {
        val mockedUserResponseModel = faker.newUserResponseModel()
        val mockedRepositoryResponseModel = faker.newRepositoryResponseModel()

        mockUserResponse(mockedUserResponseModel)

        val viewModelInstance = getViewModelInstance()
        viewModelInstance.init(mockedRepositoryResponseModel)

        // We canno't add the same source to the MediatorLiveData, so we need to create a new LiveData
        mockUserResponse(mockedUserResponseModel)

        viewModelInstance.tryLoadAgain()

        verify(exactly = 2) {
            mGithubRepository.getUser(
                viewModelInstance.viewModelScope,
                mockedRepositoryResponseModel.owner.login
            )
        }
    }

    private fun mockUserResponse(userResponseModel: UserResponseModel) {
        every { mGithubRepository.getUser(any(), any()) } returns MutableLiveData(
            Result.success(
                userResponseModel
            )
        )
    }

    private fun getViewModelInstance(): RepositoryDetailViewModel =
        RepositoryDetailViewModel(mGithubRepository).also {
            // MediatorLiveData must have a observer to be updated
            it.userResponseModel.observeForever { }
        }
}