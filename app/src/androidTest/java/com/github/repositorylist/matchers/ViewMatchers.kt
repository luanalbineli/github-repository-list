package com.github.repositorylist.matchers

import androidx.annotation.IdRes
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.github.repositorylist.interactions.withViewId
import org.hamcrest.Matchers.not

fun assertDisplayed(@IdRes viewId: Int): ViewInteraction =
    withViewId(viewId).check(matches(isDisplayed()))

fun assertNotDisplayed(@IdRes viewId: Int): ViewInteraction =
    withViewId(viewId).check(matches(not(isDisplayed())))