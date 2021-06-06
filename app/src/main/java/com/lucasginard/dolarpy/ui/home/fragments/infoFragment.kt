package com.lucasginard.dolarpy.ui.home.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
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
        configureOnClickListener()
        return _binding.root
    }

    private fun configureOnClickListener(){
        _binding.linearTextReport.setOnClickListener {
            if (_binding.linearForm.visibility == View.GONE){
                _binding.linearForm.visibility = View.VISIBLE
                _binding.ivArrow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_up))
            }else{
                _binding.linearForm.visibility = View.GONE
                _binding.ivArrow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_down))
            }
        }

        _binding.btnSend.setOnClickListener {
            if(isValid()){
                Log.d("testValid","si")
            }
        }

        _binding.linearGit.setOnClickListener {
            val uri: Uri = Uri.parse("https://github.com/melizeche/dolarPy")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

    }

    private fun isValid(): Boolean {
        return if (_binding.tvArea.editText?.text.isNullOrEmpty()){
            _binding.tvArea.error = "Ingrese"
            false
        }else{
            true
        }
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