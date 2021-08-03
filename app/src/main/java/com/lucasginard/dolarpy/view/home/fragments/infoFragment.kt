package com.lucasginard.dolarpy.view.home.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lucasginard.dolarpy.BuildConfig
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.utils.Tools
import com.lucasginard.dolarpy.databinding.FragmentInfoBinding
import com.lucasginard.dolarpy.utils.DialogConfig


class infoFragment : Fragment() {

    private lateinit var _binding:FragmentInfoBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        configureUI()
        configureOnClickListener()
        return _binding.root
    }

    private fun configureUI(){
        Tools.rotarImagen(_binding.ivIcon)
        _binding.tvVersion.text = "${getString(R.string.tvVersion)}${BuildConfig.VERSION_NAME}"
        preferences = requireActivity().getSharedPreferences("saveSettings", Context.MODE_PRIVATE)
    }

    private fun configureOnClickListener(){

        _binding.linearTextReport.setOnClickListener {
            if (_binding.linearForm.visibility == View.GONE){
                _binding.linearForm.visibility = View.VISIBLE
                _binding.tvArea.requestFocus()
                _binding.ivArrow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_up))
            }else{
                _binding.linearForm.visibility = View.GONE
                _binding.ivArrow.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrow_down))
            }
        }

        _binding.btnSend.setOnClickListener {
            var emails = arrayOf("contactolucasginard@gmail.com")
            if(isValid()){
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, emails)
                    putExtra(Intent.EXTRA_SUBJECT, "Report Bug/Error")
                    putExtra(Intent.EXTRA_TEXT, _binding.tvArea.editText?.text)
                }
                startActivity(intent)
            }
        }

        _binding.linearGit.setOnClickListener {
            val uri: Uri = Uri.parse("https://github.com/melizeche/dolarPy")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        _binding.tvTwitter.setOnClickListener {
            val uri: Uri = Uri.parse("https://twitter.com/DolarPy")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        _binding.btnConfigure.setOnClickListener {
            Tools.rotarImagen(_binding.btnConfigure)
            val dialog = DialogConfig(requireContext(),activity,preferences)
            dialog.show()
        }
    }

    private fun isValid(): Boolean {
        return if (_binding.tvArea.editText?.text.isNullOrEmpty()){
            _binding.tvArea.error = getString(R.string.textError)
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