package com.github.repositorylist.interactions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId

fun withViewId(viewId: Int): ViewInteraction = onView(withId(viewId))

fun clickOnView(viewId: Int): ViewInteraction = withViewId(viewId).perform(click())
