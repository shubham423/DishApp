package com.shubham.dishapp.model.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.shubham.dishapp.model.entities.FavDish
import com.shubham.dishapp.model.entities.RandomDish
import com.shubham.dishapp.model.network.RandomDishAPI
import com.shubham.dishapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class FavDishRepository  @Inject constructor(private val favDishDao: FavDishDao, private val api:RandomDishAPI) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish) {
        favDishDao.updateFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish) {
        favDishDao.deleteFavDishDetails(favDish)
    }

    val favoriteDishes: Flow<List<FavDish>> = favDishDao.getFavoriteDishesList()

    fun filteredListDishes(value: String): Flow<List<FavDish>> =
            favDishDao.getFilteredDishesList(value)

    suspend fun getRandomDish(): Response<RandomDish.Recipes> {
        return api.getRandomDish(
            Constants.API_KEY_VALUE,
            Constants.LIMIT_LICENSE_VALUE,
            Constants.TAGS_VALUE,
            Constants.NUMBER_VALUE
        )
    }

}