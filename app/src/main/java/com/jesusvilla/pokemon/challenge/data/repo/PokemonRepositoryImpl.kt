package com.jesusvilla.pokemon.challenge.data.repo

import com.jesusvilla.pokemon.challenge.data.local.FavoritePokemonDao
import com.jesusvilla.pokemon.challenge.data.local.FavoritePokemonEntity
import com.jesusvilla.pokemon.challenge.data.remote.PokeApiService
import com.jesusvilla.pokemon.challenge.domain.model.PokemonDetail
import com.jesusvilla.pokemon.challenge.domain.model.PokemonListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService,
    private val favDao: FavoritePokemonDao
) : PokemonRepository {

    override suspend fun getAllPokemon(): List<PokemonListItem> {
        val response = api.getPokemonList(offset = 0, limit = 2000)
        return response.results.map { r ->
            val id = r.url.trimEnd('/').substringAfterLast('/').toInt()
            PokemonListItem(
                id = id,
                name = r.name,
                spriteUrl = spriteFromId(id),
                isFavorite = false
            )
        }
    }

    override suspend fun getDetail(id: Int): PokemonDetail {
        val dto = api.getPokemonDetail(id.toString())
        val species = api.getPokemonSpecies(dto.id)

        val image = dto.sprites.other?.`official-artwork`?.front_default
            ?: dto.sprites.front_default
            ?: spriteFromId(dto.id)

        val desc = species.flavor_text_entries
            .firstOrNull { it.language.name == "en" }
            ?.flavor_text
            ?.replace("\n", " ")
            ?.replace("\u000c", " ")
            ?.trim()
            ?: ""

        val types = dto.types.sortedBy { it.slot }.map { it.type.name }
        val abilities = dto.abilities.sortedBy { it.slot }.map { it.ability.name }
        val movesPreview = dto.moves.take(2).map { it.move.name }

        fun statKeyOf(apiName: String): PokemonDetail.StatKey = when (apiName) {
            "hp" -> PokemonDetail.StatKey.HP
            "attack" -> PokemonDetail.StatKey.ATK
            "defense" -> PokemonDetail.StatKey.DEF
            "special-attack" -> PokemonDetail.StatKey.SATK
            "special-defense" -> PokemonDetail.StatKey.SDEF
            "speed" -> PokemonDetail.StatKey.SPD
            else -> PokemonDetail.StatKey.HP
        }

        val stats = dto.stats.map { PokemonDetail.Stat(statKeyOf(it.stat.name), it.base_stat) }

        return PokemonDetail(
            id = dto.id,
            name = dto.name,
            imageUrl = image,
            heightDm = dto.height,
            weightHg = dto.weight,
            types = types,
            abilities = abilities,
            movesPreview = movesPreview,
            description = desc,
            stats = stats
        )
    }

    
override fun favoriteItems(): Flow<List<PokemonListItem>> =
    favDao.observeAll().map { list ->
        list.map { e ->
            PokemonListItem(
                id = e.id,
                name = e.name,
                spriteUrl = e.spriteUrl,
                isFavorite = true
            )
        }.sortedBy { it.id }
    }

override fun favorites(): Flow<Set<Int>> =
        favDao.observeAll().map { list -> list.map { it.id }.toSet() }

    override fun isFavorite(id: Int): Flow<Boolean> = favDao.observeIsFavorite(id)

    override suspend fun toggleFavorite(id: Int, name: String, spriteUrl: String, makeFavorite: Boolean) {
        if (makeFavorite) favDao.upsert(FavoritePokemonEntity(id, name, spriteUrl))
        else favDao.deleteById(id)
    }

    private fun spriteFromId(id: Int) =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}
