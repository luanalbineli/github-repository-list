package com.github.repositorylist

import com.github.javafaker.Faker
import com.github.repositorylist.model.response.RepositoryOwnerResponseModel
import com.github.repositorylist.model.response.RepositoryResponseModel
import com.github.repositorylist.model.response.UserResponseModel

fun Faker.newRepositoryResponseModel() = RepositoryResponseModel(
    id = number().randomDigit(),
    description = lorem().words(5).joinToString(" "),
    name = "${lorem().word()}/${lorem().word()}",
    owner = RepositoryOwnerResponseModel(
        login = name().username(),
        avatarUrl = avatar().image()
    )
)

fun Faker.newUserResponseModel() = UserResponseModel(
    name = name().fullName()
)