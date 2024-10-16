package dev.iji.test_sequenia.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.iji.data.datasource.remote.RemoteDataSource
import dev.iji.data.datasource.remote.RemoteDataSourceImpl
import dev.iji.data.repository.MoviesRepositoryImpl
import dev.iji.data.service.KinopoiskService
import dev.iji.domain.repository.MoviesRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dataModule = module {

    single<MoviesRepository> {
        MoviesRepositoryImpl(remoteDataSource = get())
    }

    single<RemoteDataSource> {
        RemoteDataSourceImpl(service = get())
    }

    single<KinopoiskService> {
        Retrofit.Builder()
            .baseUrl("https://s3-eu-west-1.amazonaws.com/sequeniatesttask/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
            .create(KinopoiskService::class.java)
    }

    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}