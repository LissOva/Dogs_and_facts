package eliza.projects.dogs.data.repository

import eliza.projects.dogs.data.api.FactItem
import eliza.projects.dogs.data.api.RetrofitClient
import eliza.projects.dogs.data.model.BreedItem
import java.io.IOException

class BreedRepository {

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val exception: IOException) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }

    suspend fun fetchBreeds(): Result<List<BreedItem>> {
        return try {
            val response = RetrofitClient.api.getBreeds()
            Result.Success(response.data)
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

    suspend fun fetchRandomFact(): Result<FactItem> {
        return try {
            val response = RetrofitClient.api.getRandomFact()
            val fact = response.data.firstOrNull()
            if (fact != null) {
                Result.Success(fact)
            } else {
                Result.Error(java.io.IOException("No facts found"))
            }
        } catch (e: java.io.IOException) {
            Result.Error(e)
        }
    }
}