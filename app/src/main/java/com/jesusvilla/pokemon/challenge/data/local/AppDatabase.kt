package com.jesusvilla.pokemon.challenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoritePokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoritePokemonDao
}
