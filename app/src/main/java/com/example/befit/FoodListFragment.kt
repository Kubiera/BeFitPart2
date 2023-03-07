package com.example.befit

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


class FoodListFragment : Fragment() {
    private val foodItems = mutableListOf<FoodItem>()
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter : FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        foodRecyclerView = view.findViewById(R.id.items)
        foodAdapter = FoodAdapter(view.context, foodItems)
        foodRecyclerView.adapter = foodAdapter
        foodRecyclerView.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItemDecoration = DividerItemDecoration(view.context, it.orientation)
            foodRecyclerView.addItemDecoration(dividerItemDecoration)
        }
        return view
    }

    private fun fetchFoodItems() {
        lifecycleScope.launch {
            (requireActivity().application as FoodApplication).db.foodItemDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    FoodItem(
                        entity.name,
                        entity.calories
                    )
                }.also { mappedList ->
                    foodItems.clear()
                    foodItems.addAll(mappedList)
                    foodAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFoodItems()
    }

    companion object {
       fun newInstance(): FoodListFragment {
            return FoodListFragment()
       }
    }
}