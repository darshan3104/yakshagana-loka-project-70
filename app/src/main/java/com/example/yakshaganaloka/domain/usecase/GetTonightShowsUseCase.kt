package com.example.yakshaganaloka.domain.usecase

import com.example.yakshaganaloka.data.model.PerformanceEvent
import com.example.yakshaganaloka.data.repository.PerformanceRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetTonightShowsUseCase @Inject constructor(
    private val repository: PerformanceRepository
) {
    operator fun invoke(): Flow<Result<List<PerformanceEvent>>> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return repository.getTonightShows(today)
    }
}
