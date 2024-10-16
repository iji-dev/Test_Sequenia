package dev.iji.data.repository

import dev.iji.data.datasource.remote.RemoteDataSource
import dev.iji.data.mapper.MoviesMapper.toMovieModel
import dev.iji.domain.model.MovieModel
import dev.iji.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override suspend fun getMovies(): Result<List<MovieModel>> = runCatching {
        remoteDataSource.getMovies().body()?.movies?.map { it.toMovieModel() } ?: emptyList()
    }
}