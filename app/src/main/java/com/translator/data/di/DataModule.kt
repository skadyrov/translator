package com.translator.data.di

import com.translator.data.DefaultTranslationRepository
import com.translator.data.TranslationRepository
import com.translator.data.remote.TranslatorApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideTranslatorRepository(translatorApi: TranslatorApi): TranslationRepository {
        return DefaultTranslationRepository(translatorApi)
    }

    @Provides
    fun createTranslatorApi(retrofit: Retrofit): TranslatorApi {
        return retrofit.create(TranslatorApi::class.java)
    }
}