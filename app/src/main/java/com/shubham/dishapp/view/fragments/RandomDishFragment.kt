package com.shubham.dishapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shubham.dishapp.R

class RandomDishFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_random_dish, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        textView.text = "Random Dish Fragment"
        return root
    }
}