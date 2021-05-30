package com.lucasginard.dolarpy.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucasginard.dolarpy.databinding.FragmentUbicacionBinding


class ubicacionFragment : Fragment() {

    private lateinit var _binding :FragmentUbicacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUbicacionBinding.inflate(inflater, container, false)
        return _binding.root
    }

    companion object {
        fun newInstance() =
            ubicacionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}