package com.example.yakshaganaloka.data.repository

import com.example.yakshaganaloka.data.local.LocalJsonDataSource
import com.example.yakshaganaloka.data.model.PerformanceEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceRepository @Inject constructor(
    private val localJsonDataSource: LocalJsonDataSource
) {
    fun getTonightShows(date: String): Flow<Result<List<PerformanceEvent>>> = flow {
        try {
            val allEvents = localJsonDataSource.loadPerformanceEvents()
            var localEvents = allEvents.filter { it.event_date == date }
            
            // Fallback for demo purposes if no shows match today's date
            if (localEvents.isEmpty() && allEvents.isNotEmpty()) {
                localEvents = allEvents.sortedBy { it.event_date }.take(5)
            }
            
            emit(Result.success(localEvents))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
