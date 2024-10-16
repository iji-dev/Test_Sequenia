package dev.iji.data.service

import dev.iji.data.dto.MoviesDto
import retrofit2.Response
import retrofit2.http.GET

interface KinopoiskService {

    @GET("films.json")
    suspend fun getMovies(): Response<MoviesDto>
}