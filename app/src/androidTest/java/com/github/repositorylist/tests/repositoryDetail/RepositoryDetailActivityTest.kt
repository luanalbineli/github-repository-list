package com.github.repositorylist.tests.repositoryDetail

import androidx.lifecycle.MutableLiveData
import androidx.test.rule.ActivityTestRule
import com.bumptech.glide.load.HttpException
import com.github.repositorylist.R
import com.github.repositorylist.di.BindingModule
import com.github.repositorylist.interactions.clickOnView
import com.github.repositorylist.matchers.assertDisplayed
import com.github.repositorylist.matchers.assertNotDisplayed
import com.github.repositorylist.matchers.assertWithText
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.newRepositoryResponseModel
import com.github.repositorylist.newUserResponseModel
import com.github.repositorylist.repository.github.IGithubRepository
import com.github.repositorylist.tests.common.InstrumentedTest
import com.github.repositorylist.ui.detail.RepositoryDetailActivity
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@UninstallModules(BindingModule::class)
@HiltAndroidTest
class RepositoryDetailActivityTest : InstrumentedTest() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mActivityRule = ActivityTestRule(RepositoryDetailActivity::class.java, false, false)

    @BindValue
    lateinit var mGithubRepository: IGithubRepository

    @Before
    fun setup() {
        mGithubRepository = Mockito.mock(IGithubRepository::class.java)

        hiltRule.inject()
    }

    @Test
    fun showRepositoryDetailTest() {
        val mockedRepositoryResponseModel = faker.newRepositoryResponseModel()
        val mockedUserResponseModel = faker.newUserResponseModel()

        whenever(mGithubRepository.getUser(any(), any())).thenReturn(
            MutableLiveData(
                Result.success(
                    mockedUserResponseModel
                )
            )
        )

        val intent = RepositoryDetailActivity.getIntent(context, mockedRepositoryResponseModel)
        mActivityRule.launchActivity(intent)

        assertDisplayed(R.id.container_repository_detail_data)
        assertNotDisplayed(R.id.request_status_view_repository_detail)

        assertWithText(R.id.text_repository_user_name, mockedUserResponseModel.name)
        assertWithText(R.id.text_repository_detail_user_login, mockedRepositoryResponseModel.name)
        assertWithText(
            R.id.text_repository_detail_description,
            mockedRepositoryResponseModel.description
        )

        verify(mGithubRepository).getUser(any(), eq(mockedRepositoryResponseModel.owner.login))
    }

    @Test
    fun errorFetchUserDetailTest() {
        val mockedRepositoryResponseModel = faker.newRepositoryResponseModel()
        val mockedUserResponseModel = faker.newUserResponseModel()

        whenever(mGithubRepository.getUser(any(), any())).thenReturn(
            MutableLiveData(
                Result.error(
                    HttpException(400)
                )
            )
        )

        val intent = RepositoryDetailActivity.getIntent(context, mockedRepositoryResponseModel)
        mActivityRule.launchActivity(intent)

        assertDisplayed(R.id.request_status_view_repository_detail)
        assertNotDisplayed(R.id.container_repository_detail_data)

        verify(mGithubRepository).getUser(any(), eq(mockedRepositoryResponseModel.owner.login))

        whenever(mGithubRepository.getUser(any(), any())).thenReturn(
            MutableLiveData(
                Result.success(
                    mockedUserResponseModel
                )
            )
        )

        clearInvocations(mGithubRepository)

        clickOnView(R.id.button_request_status_try_again)

        assertDisplayed(R.id.container_repository_detail_data)
        assertNotDisplayed(R.id.request_status_view_repository_detail)

        verify(mGithubRepository).getUser(any(), eq(mockedRepositoryResponseModel.owner.login))
    }
}