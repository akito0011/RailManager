package com.example.trainmanager.utils

import androidx.lifecycle.MutableLiveData
import com.example.trainmanager.persistence.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T> MutableLiveData<T>.forceRefresh() {
    this.value = this.value
}
