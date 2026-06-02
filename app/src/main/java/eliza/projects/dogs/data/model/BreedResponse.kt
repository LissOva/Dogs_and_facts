package eliza.projects.dogs.data.model

import com.google.gson.annotations.SerializedName

data class BreedsResponse(
    val data: List<BreedItem>,
    val meta: PaginationMeta? = null,
    val links: Links? = null
)

data class BreedItem(
    val id: String,
    val type: String,
    val attributes: BreedAttributes
)

data class BreedAttributes(
    val name: String,
    val description: String? = null,
    val hypoallergenic: Boolean = false,
    val life: LifeSpan? = null,
    @SerializedName("male_weight") val maleWeight: Weight? = null,
    @SerializedName("female_weight") val femaleWeight: Weight? = null
)

data class LifeSpan(
    val min: Int? = null,
    val max: Int? = null
)

data class Weight(
    val min: Int? = null,
    val max: Int? = null
)

data class PaginationMeta(
    val pagination: Pagination? = null
)

data class Pagination(
    val current: Int? = null,
    val records: Int? = null
)

data class Links(
    val self: String? = null,
    val next: String? = null
)