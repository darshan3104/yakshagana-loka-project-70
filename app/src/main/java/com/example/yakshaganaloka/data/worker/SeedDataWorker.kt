package com.example.yakshaganaloka.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.yakshaganaloka.domain.usecase.SeedDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SeedDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val seedDataUseCase: SeedDataUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            seedDataUseCase.invoke()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
