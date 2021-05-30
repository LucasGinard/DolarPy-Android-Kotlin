package com.lucasginard.dolarpy.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.databinding.FragmentInfoBinding


class infoFragment : Fragment() {

    private lateinit var _binding:FragmentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return _binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            infoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}