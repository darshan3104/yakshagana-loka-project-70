package com.example.yakshaganaloka.data.repository

import com.example.yakshaganaloka.data.local.LocalJsonDataSource
import com.example.yakshaganaloka.data.model.AudioClip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject constructor(
    private val localJsonDataSource: LocalJsonDataSource
) {
    fun getAudioClips(): Flow<Result<List<AudioClip>>> = flow {
        try {
            val localClips = localJsonDataSource.loadAudioClips()
            emit(Result.success(localClips))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
