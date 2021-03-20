package com.shubham.dishapp.view.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.dishapp.R
import com.shubham.dishapp.databinding.DialogCustomListBinding
import com.shubham.dishapp.databinding.FragmentAllDishesBinding
import com.shubham.dishapp.model.entities.FavDish
import com.shubham.dishapp.utils.Constants
import com.shubham.dishapp.view.activities.AddUpdateDishActivity
import com.shubham.dishapp.view.activities.MainActivity
import com.shubham.dishapp.view.adapters.CustomListItemAdapter
import com.shubham.dishapp.view.adapters.FavDishAdapter
import com.shubham.dishapp.viewmodel.FavDishViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding

    private val mFavDishViewModel: FavDishViewModel by viewModels()

    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var mCustomListDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mBinding =
                FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this@AllDishesFragment)
        mBinding.rvDishesList.adapter = mFavDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                if (it.isNotEmpty()) {

                    mBinding.rvDishesList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE

                    mFavDishAdapter.dishesList(it)
                } else {

                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    fun dishDetails(favDish: FavDish) {

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

        findNavController()
                .navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(favDish))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_filter_dishes -> {
                filterDishesListDialog()
                return true
            }

            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteDish(dish: FavDish) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog, dish.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            mFavDishViewModel.delete(dish)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog() {
        mCustomListDialog = Dialog(requireActivity())

        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(
                requireActivity(),
                this@AllDishesFragment,
                dishTypes,
                Constants.FILTER_SELECTION
        )
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun filterSelection(filterItemSelection: String) {

        mCustomListDialog.dismiss()

        Log.i("Filter Selection", filterItemSelection)

        if (filterItemSelection == Constants.ALL_ITEMS) {
            mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {

                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE

                        mFavDishAdapter.dishesList(it)
                    } else {

                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        } else {

            mFavDishViewModel.getFilteredList(filterItemSelection)
                    .observe(viewLifecycleOwner) { dishes ->
                        dishes.let {
                            if (it.isNotEmpty()) {

                                mBinding.rvDishesList.visibility = View.VISIBLE
                                mBinding.tvNoDishesAddedYet.visibility = View.GONE

                                mFavDishAdapter.dishesList(it)
                            } else {

                                mBinding.rvDishesList.visibility = View.GONE
                                mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                            }
                        }
                    }
        }
    }
}