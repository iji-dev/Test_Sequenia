package dev.iji.test_sequenia.di

import dev.iji.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<MainViewModel> {
        MainViewModel(getMovies = get())
    }
}