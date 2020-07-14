package com.github.repositorylist.tests

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.javafaker.Faker

abstract class InstrumentedTest {
    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val faker by lazy { Faker() }
}
