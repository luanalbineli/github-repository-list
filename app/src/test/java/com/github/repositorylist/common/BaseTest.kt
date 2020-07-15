package com.github.repositorylist.common

import com.github.javafaker.Faker
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(LiveDataInstantExecutorExtension::class)
abstract class BaseTest {
    protected val faker by lazy { Faker() }
}