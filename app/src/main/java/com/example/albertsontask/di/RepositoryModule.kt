package com.example.albertsontask.di

import com.example.albertsontask.data.repository.RepositoryImp
import com.example.albertsontask.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryImp: RepositoryImp): Repository
}