package com.shubham.dishapp.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.shubham.dishapp.model.database.FavDishRepository
import com.shubham.dishapp.model.entities.FavDish
import kotlinx.coroutines.launch


class FavDishViewModel @ViewModelInject constructor(private val repository: FavDishRepository, application:Application) : AndroidViewModel(application) {

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }

    val favoriteDishes: LiveData<List<FavDish>> = repository.favoriteDishes.asLiveData()

    fun delete(dish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishData(dish)
    }

    fun getFilteredList(value: String): LiveData<List<FavDish>> = repository.filteredListDishes(value).asLiveData()
}

