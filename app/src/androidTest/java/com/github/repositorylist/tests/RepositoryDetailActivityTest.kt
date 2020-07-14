package com.github.repositorylist.tests

import androidx.lifecycle.MutableLiveData
import androidx.test.rule.ActivityTestRule
import com.github.repositorylist.R
import com.github.repositorylist.di.BindingModule
import com.github.repositorylist.matchers.assertWithText
import com.github.repositorylist.model.common.Result
import com.github.repositorylist.model.response.RepositoryOwnerResponseModel
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.UserResponseModel
import com.github.repositorylist.repository.github.IGithubRepository
import com.github.repositorylist.ui.detail.RepositoryDetailActivity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
        val mockedRepositoryResponseModel = RepositoryResponseModel(
            id = faker.number().randomDigit(),
            description = faker.lorem().words(5).joinToString(" "),
            name = "${faker.lorem().word()}/${faker.lorem().word()}",
            owner = RepositoryOwnerResponseModel(
                login = faker.name().username(),
                avatarUrl = faker.avatar().image()
            )
        )

        val userResponseModel = UserResponseModel(
            name = faker.name().fullName()
        )

        whenever(mGithubRepository.getUser(any(), any())).thenReturn(MutableLiveData(Result.success(userResponseModel)))

        val intent = RepositoryDetailActivity.getIntent(context, mockedRepositoryResponseModel)
        mActivityRule.launchActivity(intent)

        assertWithText(R.id.text_repository_user_name, userResponseModel.name)
        assertWithText(R.id.text_repository_detail_user_login, mockedRepositoryResponseModel.name)
        assertWithText(R.id.text_repository_detail_description, mockedRepositoryResponseModel.description)

        verify(mGithubRepository).getUser(any(), eq(mockedRepositoryResponseModel.owner.login))
    }
}