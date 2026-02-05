package com.jesusvilla.pokemon.challenge.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePokemonDao {

    @Query("SELECT * FROM favorite_pokemon")
    fun observeAll(): Flow<List<FavoritePokemonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = :id)")
    fun observeIsFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoritePokemonEntity)

    @Query("DELETE FROM favorite_pokemon WHERE id = :id")
    suspend fun deleteById(id: Int)
}
