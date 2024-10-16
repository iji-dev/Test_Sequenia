package dev.iji.test_sequenia.di

import dev.iji.domain.usecases.GetMovies
import org.koin.dsl.module

val domainModule = module {

    factory<GetMovies> {
        GetMovies(moviesRepository = get())
    }
}