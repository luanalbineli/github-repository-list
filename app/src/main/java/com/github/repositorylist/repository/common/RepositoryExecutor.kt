package com.github.repositorylist.repository.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.repositorylist.model.common.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class RepositoryExecutor @Inject constructor(
    private val retrofit: Retrofit
) {
    fun <R, T> makeRequest(
        clazz: Class<T>,
        viewModelScope: CoroutineScope,
        asyncRequest: suspend CoroutineScope.(serviceInstance: T) -> R
    ): LiveData<Result<R>> {
        val result = MutableLiveData<Result<R>>()
        result.value = Result.loading()

        viewModelScope.launch {
            try {
                var asyncRequestResult: R? = null
                withContext(Dispatchers.Default) {
                    val apiServiceInstance = retrofit.create(clazz)
                    asyncRequestResult = asyncRequest(apiServiceInstance)
                }
                result.value = Result.success(asyncRequestResult)
            } catch (exception: Exception) {
                result.value = Result.error(exception)
            }
        }

        return result
    }
}