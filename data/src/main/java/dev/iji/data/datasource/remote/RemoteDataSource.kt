package dev.iji.data.datasource.remote

import dev.iji.data.dto.MoviesDto
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getMovies(): Response<MoviesDto>
}