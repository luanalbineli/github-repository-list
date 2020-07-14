package com.github.repositorylist.matchers

import androidx.annotation.IdRes
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.repositorylist.interactions.withViewId

fun assertWithText(@IdRes id: Int, text: String?): ViewInteraction =
    withViewId(id).check(ViewAssertions.matches(ViewMatchers.withText(text)))