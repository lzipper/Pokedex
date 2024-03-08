package com.example.pokemonapp.model.pokemon

data class TypesResponse(
    val damage_relations: DamageRelations,
    val game_indices: List<GameIndiceX>,
    val generation: GenerationXX,
    val id: Int,
    val move_damage_class: MoveDamageClassX,
    val moves: List<MoveX>,
    val name: String,
    val names: List<NameX>,
    val past_damage_relations: List<PastDamageRelation>,
    val pokemon: List<PokemonX>
)