package dev.iji.data.datasource.remote

import dev.iji.data.dto.MoviesDto
import dev.iji.data.service.KinopoiskService
import retrofit2.Response

class RemoteDataSourceImpl(private val service: KinopoiskService) : RemoteDataSource {

    override suspend fun getMovies(): Response<MoviesDto> = service.getMovies()
}