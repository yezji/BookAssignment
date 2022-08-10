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

        viewModel.fragmentType.postValue(FragmentEnum.SearchMain)
//        viewModel.keyword.postValue("가") // TODO: 기본 아무것도 없을 때 처리

        initUI()


        // prepare fragment instance
        searchMainFragment = SearchMainFragment()
        // set transaction of sfm
        transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(binding.flMainContent.id, searchMainFragment, FragmentEnum.SearchMain.resString)
            .addToBackStack(null)
            .commit()

    }

    private fun initUI() {
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
            if (keyword != "") viewModel.getSearchBookList(keyword, "accuracy", 1, 50, "title")
            else viewModel.clearSearchBookList()
        }


        viewModel.isProgressVisible.observe(this) { isVisible ->
            // set progressBar visibility
            binding.progressBarMain.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        // set toolbar
        viewModel.fragmentType.observe(this) { enum ->
            when (enum) {
                FragmentEnum.SearchMain -> {
                    binding.svSearchMain.visibility = View.VISIBLE
                    binding.tbNormalMain.visibility = View.GONE
                }
                else -> {
                    binding.svSearchMain.visibility = View.GONE
                    binding.tbNormalMain.visibility = View.VISIBLE

                    binding.ibNormalBack.setOnClickListener {
                        supportFragmentManager.popBackStack()
                    }
                    binding.ibNormalLike.setOnClickListener {
                        // TODO: like 처리

                    }
                }
            }
        }
    }
}
