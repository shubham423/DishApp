package com.shubham.dishapp.application

import android.app.Application
import com.shubham.dishapp.model.database.FavDishRepository
import com.shubham.dishapp.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {

    private val database by lazy { FavDishRoomDatabase.getDatabase(this@FavDishApplication) }

    val repository by lazy { FavDishRepository(database.favDishDao()) }
}