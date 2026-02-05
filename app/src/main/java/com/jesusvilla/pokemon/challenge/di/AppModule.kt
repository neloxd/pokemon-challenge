package com.jesusvilla.pokemon.challenge.di

import android.content.Context
import androidx.room.Room
import com.jesusvilla.pokemon.challenge.data.local.AppDatabase
import com.jesusvilla.pokemon.challenge.data.local.FavoritePokemonDao
import com.jesusvilla.pokemon.challenge.data.remote.PokeApiService
import com.jesusvilla.pokemon.challenge.data.repo.PokemonRepository
import com.jesusvilla.pokemon.challenge.data.repo.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDbModule {

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient {
        val log = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(log)
            .build()
    }

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): PokeApiService =
        retrofit.create(PokeApiService::class.java)

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "pokedex.db").build()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoritePokemonDao = db.favoriteDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds abstract fun bindRepo(impl: PokemonRepositoryImpl): PokemonRepository
}
