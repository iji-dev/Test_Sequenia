package dev.iji.domain.usecases

import dev.iji.domain.repository.MoviesRepository

class GetMovies(private val moviesRepository: MoviesRepository) {

    suspend operator fun invoke() = moviesRepository.getMovies()
}