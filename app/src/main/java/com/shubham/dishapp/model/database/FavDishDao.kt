package com.shubham.dishapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shubham.dishapp.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)
    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>
}