package eliza.projects.dogs.data.api

import eliza.projects.dogs.data.model.BreedsResponse
import retrofit2.http.GET
import retrofit2.http.Query

data class FactResponse(
    val data: List<FactItem>
)

data class FactItem(
    val id: String,
    val type: String,
    val attributes: FactAttributes
)

data class FactAttributes(
    val body: String
)

interface DogApiService {
    //Получить все породы
    @GET("breeds")
    suspend fun getBreeds(
        @Query("page[number]") page: Int = 1,
        @Query("page[size]") size: Int = 1000
    ): BreedsResponse

    //Получить рандомный факт
    @GET("facts")
    suspend fun getRandomFact(
        @Query("limit") limit: Int = 1
    ): FactResponse
}