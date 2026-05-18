package com.example.yakshaganaloka.data.repository

import com.example.yakshaganaloka.data.local.LocalJsonDataSource
import com.example.yakshaganaloka.data.model.Mela
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MelaRepository @Inject constructor(
    private val localJsonDataSource: LocalJsonDataSource
) {
    fun getMelas(): Flow<Result<List<Mela>>> = flow {
        try {
            val localMelas = localJsonDataSource.loadMelas()
            emit(Result.success(localMelas))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
