package com.example.todoapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.databinding.FragmentPriorityBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PriorityBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var onPrioritySelectedListener: (String) -> Unit
    private var _binding: FragmentPriorityBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    fun setOnPrioritySelectedListener(listener: (String) -> Unit) {
        onPrioritySelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPriorityBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.buttonHighPriority.setOnClickListener {
            onPrioritySelectedListener("!! Высокий")
            dismiss()
        }

        binding.buttonLowPriority.setOnClickListener {
            onPrioritySelectedListener("Низкий")
            dismiss()
        }

        binding.buttonNoPriority.setOnClickListener {
            onPrioritySelectedListener("Нет")
            dismiss()
        }
    }
}