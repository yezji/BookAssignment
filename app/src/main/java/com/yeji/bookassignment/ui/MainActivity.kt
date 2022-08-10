package com.yeji.bookassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.yeji.bookassignment.App
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.databinding.ActivityMainBinding
import com.yeji.bookassignment.viewmodel.MainViewModel
import com.yeji.bookassignment.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)
    private lateinit var viewModel: MainViewModel
    private lateinit var transaction: FragmentTransaction
    private lateinit var searchMainFragment: SearchMainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(repository = (application as App).repository))
            .get(MainViewModel::class.java)

        // prepare fragment instance
        searchMainFragment = SearchMainFragment()
        // set transaction of sfm
        transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(binding.flMainContent.id, searchMainFragment, FragmentEnum.SearchMain.resString)
            .commit()
        viewModel.fragmentType.postValue(FragmentEnum.SearchMain)
        viewModel.keyword.postValue("")

        initUI()

    }

    private fun initUI() {
        // TODO: init
        // set toolbar

        binding.svSearchMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.keyword.postValue(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.keyword.observe(this) { keyword ->
            viewModel.getSearchBookList(keyword, null, null, 50, null)
        }

        viewModel.isProgressVisible.observe(this) { isVisible ->
            // set progressBar visibility
            binding.progressBarMain.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        viewModel.fragmentType.observe(this) { enum ->
            when (enum) {
                FragmentEnum.SearchMain -> {
                    binding.svSearchMain.visibility = View.VISIBLE
                    binding.tbNormalMain.visibility = View.GONE
                }
                else -> {
                    binding.svSearchMain.visibility = View.GONE
                    binding.tbNormalMain.visibility = View.VISIBLE
                }
            }
        }
    }
}
