package dev.iji.domain.repository

import dev.iji.domain.model.MovieModel

interface MoviesRepository {

    suspend fun getMovies(): Result<List<MovieModel>>
}