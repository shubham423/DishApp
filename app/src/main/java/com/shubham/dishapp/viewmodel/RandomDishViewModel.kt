package com.shubham.dishapp.viewmodel


import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.dishapp.model.database.FavDishRepository
import com.shubham.dishapp.model.entities.RandomDish
import kotlinx.coroutines.launch

class RandomDishViewModel @ViewModelInject constructor(  private val repo:FavDishRepository,application: Application) :AndroidViewModel(application) {



    val loadRandomDish = MutableLiveData<Boolean>()
    val randomDishResponse = MutableLiveData<RandomDish.Recipes>()
    val randomDishLoadingError = MutableLiveData<Boolean>()

        fun getRandomDishFromAPI() {
            viewModelScope.launch {
                loadRandomDish.value = true

                var result=repo.getRandomDish()
                if (result!=null&& result.isSuccessful){
                    loadRandomDish.value = false
                    randomDishResponse.value = result.body()
                    randomDishLoadingError.value = false
                }
                else{
                    loadRandomDish.value = false
                    randomDishLoadingError.value = true
                }
            }
        }

}