package com.yeji.bookassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.yeji.bookassignment.App
import com.yeji.bookassignment.databinding.ActivityMainBinding
import com.yeji.bookassignment.viewmodel.MainViewModel
import com.yeji.bookassignment.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(repository = (application as App).repository))
            .get(MainViewModel::class.java)

        initUI()

        viewModel.keyword.postValue("")
    }

    private fun initUI() {
        // TODO: init

        viewModel.keyword.observe(this) { keyword ->
            viewModel.getSearchBookList(keyword, null, null, 50, null)
        }

        viewModel.isProgressVisible.observe(this) { isVisible ->
            // set progressBar visibility
        }
    }
}
