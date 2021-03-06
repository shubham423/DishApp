package com.shubham.dishapp.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.shubham.dishapp.R
import com.shubham.dishapp.databinding.FragmentRandomDishBinding
import com.shubham.dishapp.model.entities.FavDish
import com.shubham.dishapp.model.entities.RandomDish
import com.shubham.dishapp.utils.Constants
import com.shubham.dishapp.viewmodel.FavDishViewModel
import com.shubham.dishapp.viewmodel.RandomDishViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RandomDishFragment : Fragment() {

    private var mBinding: FragmentRandomDishBinding? = null
    private var mProgressDialog: Dialog? = null

    private lateinit var mRandomDishViewModel: RandomDishViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRandomDishViewModel =
                ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomDishFromAPI()

        randomDishViewModelObserver()

        mBinding!!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomDishFromAPI()
        }
    }

    private fun randomDishViewModelObserver() {

        mRandomDishViewModel.randomDishResponse.observe(
                viewLifecycleOwner,
                Observer { randomDishResponse ->
                    randomDishResponse?.let {
                        Log.i("Random Dish Response", "${randomDishResponse.recipes[0]}")

                        if (mBinding!!.srlRandomDish.isRefreshing) {
                            mBinding!!.srlRandomDish.isRefreshing = false
                        }
                        setRandomDishResponseInUI(randomDishResponse.recipes[0])
                    }
                })

        mRandomDishViewModel.randomDishLoadingError.observe(
                viewLifecycleOwner,
                Observer { dataError ->
                    dataError?.let {
                        Log.i("Random Dish API Error", "$dataError")

                        if (mBinding!!.srlRandomDish.isRefreshing) {
                            mBinding!!.srlRandomDish.isRefreshing = false
                        }
                    }
                })

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner, Observer { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random Dish Loading", "$loadRandomDish")
                if (loadRandomDish && !mBinding!!.srlRandomDish.isRefreshing) {
                    showCustomProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        })
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {
        Glide.with(requireActivity())
                .load(recipe.image)
                .centerCrop()
                .into(mBinding!!.ivDishImage)

        mBinding!!.tvTitle.text = recipe.title

        var dishType: String = "other"

        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            mBinding!!.tvType.text = dishType
        }

        mBinding!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {

            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        mBinding!!.tvIngredients.text = ingredients

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding!!.tvCookingDirection.text = Html.fromHtml(
                    recipe.instructions,
                    Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            mBinding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        mBinding!!.tvCookingTime.text =
                resources.getString(
                        R.string.lbl_estimate_cooking_time,
                        recipe.readyInMinutes.toString()
                )

        mBinding!!.ivFavoriteDish.setImageDrawable(
                ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                )
        )

        var addedToFavorite = false


        mBinding!!.ivFavoriteDish.setOnClickListener {
            if (addedToFavorite) {
                Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_already_added_to_favorites),
                        Toast.LENGTH_SHORT
                ).show()
            } else {

                val randomDishDetails = FavDish(
                        recipe.image,
                        Constants.DISH_IMAGE_SOURCE_ONLINE,
                        recipe.title,
                        dishType,
                        "Other",
                        ingredients,
                        recipe.readyInMinutes.toString(),
                        recipe.instructions,
                        true
                )

                val mFavDishViewModel: FavDishViewModel by viewModels()


                mFavDishViewModel.insert(randomDishDetails)

                addedToFavorite = true

                mBinding!!.ivFavoriteDish.setImageDrawable(
                        ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_favorite_selected
                        )
                )

                Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_added_to_favorites),
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog?.let {

            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideProgressDialog() {
        mProgressDialog?.let {
            it.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}