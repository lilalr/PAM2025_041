package com.example.pam_florify.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pam_florify.repositori.AplikasiFlorify
fun CreationExtras.aplikasiFlorify(): AplikasiFlorify =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiFlorify)

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                aplikasiFlorify().container.repositoryUser
            )
        }

        initializer {
            RegisterViewModel(
                aplikasiFlorify().container.repositoryUser
            )
        }

        initializer {
            HomeViewModel(
                aplikasiFlorify().container.repositoryTanaman
            )
        }

        initializer {
            EntryViewModel(
                aplikasiFlorify().container.repositoryTanaman
            )
        }

        initializer {
            DetailTanamanViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                repositoryTanaman = aplikasiFlorify().container.repositoryTanaman
            )
        }

        initializer {
            EditTanamanViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                repositoryTanaman = aplikasiFlorify().container.repositoryTanaman
            )
        }
    }
}