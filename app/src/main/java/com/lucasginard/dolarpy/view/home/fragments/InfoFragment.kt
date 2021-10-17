package com.lucasginard.dolarpy.view.home.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lucasginard.dolarpy.BuildConfig
import com.lucasginard.dolarpy.R
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.databinding.FragmentInfoBinding
import com.lucasginard.dolarpy.domain.MainRepository
import com.lucasginard.dolarpy.utils.DialogConfig
import com.lucasginard.dolarpy.utils.Tools
import com.lucasginard.dolarpy.view.viewModel.MainViewModel
import com.lucasginard.dolarpy.view.viewModel.MyViewModelFactory


class InfoFragment : Fragment() {

    private val retrofitService = apiService.getInstance()
    private lateinit var _binding:FragmentInfoBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var dialog:DialogConfig


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
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)
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
            val emails = arrayOf("contactolucasginard@gmail.com")
            if(isValid()){
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, emails)
                    putExtra(Intent.EXTRA_SUBJECT, "Report Bug/Error")
                    putExtra(Intent.EXTRA_TEXT, _binding.tvArea.editText?.text)
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context,getString(R.string.textErrorAll),Toast.LENGTH_LONG).show()
                }
            }
        }

        _binding.linearGit.setOnClickListener {
            Tools.goToURL("https://github.com/melizeche/dolarPy",requireContext())
        }

        _binding.tvTwitter.setOnClickListener {
            Tools.goToURL("https://twitter.com/DolarPy",requireContext())
        }

        _binding.btnConfigure.setOnClickListener {
            Tools.rotarImagen(_binding.btnConfigure)
            dialog = DialogConfig(requireContext(),activity,viewModel)
            dialog.show()
        }

        _binding.tvLicense.setOnClickListener {
            Tools.goToURL("https://www.apache.org/licenses/LICENSE-2.0.txt",requireContext())
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


    override fun onPause() {
        super.onPause()
        if (::dialog.isInitialized){
            dialog.dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InfoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}