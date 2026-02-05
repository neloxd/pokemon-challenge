package com.jesusvilla.pokemon.challenge.data.remote

import com.jesusvilla.pokemon.challenge.data.remote.dto.PokemonDetailDto
import com.jesusvilla.pokemon.challenge.data.remote.dto.PokemonListResponseDto
import com.jesusvilla.pokemon.challenge.data.remote.dto.PokemonSpeciesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponseDto

    @GET("pokemon/{nameOrId}")
    suspend fun getPokemonDetail(
        @Path("nameOrId") nameOrId: String
    ): PokemonDetailDto

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesDto
}
