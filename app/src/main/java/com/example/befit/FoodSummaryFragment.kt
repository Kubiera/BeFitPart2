package com.example.befit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FoodSummaryFragment : Fragment() {
    private lateinit var minValue : TextView
    private lateinit var avgValue: TextView
    private lateinit var maxValue: TextView
    private val foodItems = mutableListOf<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_summary, container, false)
        minValue = view.findViewById(R.id.minValue)
        avgValue = view.findViewById(R.id.avgValue)
        maxValue = view.findViewById(R.id.maxValue)
        return view
    }

    private fun getValues() {
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
                    setValues()
                }
            }
        }
    }

    private fun setValues() {
        var max : Int = Int.MIN_VALUE
        var min : Int = Int.MAX_VALUE
        var total : Int = 0

        for (a in foodItems){
            val value = a.calories!!.toInt()
            if(value < min){
                min = value
            }
            if(value > max){
                max = value
            }
            total += value
        }

        val average = total / foodItems.size


        minValue.text = min.toString()
        avgValue.text = average.toString()
        maxValue.text = max.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getValues()
    }

    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }
    }
}