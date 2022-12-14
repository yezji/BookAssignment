package com.yeji.presentation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yeji.presentation.R
import com.yeji.presentation.databinding.ActivityMainBinding
import com.yeji.presentation.databinding.ToolbarSearchDetailBinding
import com.yeji.presentation.databinding.ToolbarSearchMainBinding
import com.yeji.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)
    private var _toolbarMainBinding: ToolbarSearchMainBinding? = null
    private val toolbarMainBinding: ToolbarSearchMainBinding get() = requireNotNull(_toolbarMainBinding)
    private var _toolbarDetailBinding: ToolbarSearchDetailBinding? = null
    private val toolbarDetailBinding: ToolbarSearchDetailBinding get() = requireNotNull(_toolbarDetailBinding)
    private val viewModel by viewModels<MainViewModel>()
   // private lateinit var viewModel: MainViewModel

//    private lateinit var transaction: FragmentTransaction
    private lateinit var navController: NavController
    private lateinit var searchMainFragment: SearchMainFragment

//    private var backButtonTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
//        _toolbarMainBinding = ToolbarSearchMainBinding.inflate(layoutInflater)
//        _toolbarDetailBinding = ToolbarSearchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         * comments
         * - DI ???????????? ?????? ??????
         *   ????????? ?????? ???????????? ????????? viewModel by viewModels<MainViewModel>()??? ??????
         */
//        viewModel = ViewModelProvider(this, MainViewModelFactory(repository = (application as App).repository))
//            .get(MainViewModel::class.java)


        // prepare fragment instance
      //  searchMainFragment = SearchMainFragment()

//        // set transaction of sfm
//        transaction = supportFragmentManager.beginTransaction()
//        transaction
//            .add(binding.navMainContent.id, searchMainFragment, null)
//            .commit()

        // set navigation
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navMainContent.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.toolbar.setupWithNavController(navController)
//        setupActionBarWithNavController(navController) // actionbar
//        setToolbar(fragId = R.id.fragment_search_main)

//        binding.bnv.setupWithNavController(navController) // bottomnavigationview
        navController.addOnDestinationChangedListener(this)

        initUI()
    }


    private fun initUI() {
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
                .collect {
                    showProgressBar(isVisible = it.isProgressVisible)
                    showErrorToast(errorMsg = it.loadError)
                }
        }




    }


    private fun showProgressBar(isVisible: Boolean){
        binding.progressBarMain.isVisible = isVisible
    }
    private fun showErrorToast(errorMsg: String?) {
        if (!errorMsg.isNullOrBlank()) Toast.makeText(this@MainActivity, "err: $errorMsg", Toast.LENGTH_SHORT).show()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
//        setToolbar(destination.id)
    }


//    private fun setToolbar(fragId: Int) {
//        var toolbar: Toolbar = toolbarMainBinding.tbSearchMain
//        when (fragId) {
//            R.id.fragment_search_main -> {
//                toolbar = toolbarMainBinding.tbSearchMain
//            }
//            R.id.fragment_search_detail -> {
//                toolbar = toolbarDetailBinding.tbSearchDetail
//            }
//        }
//
//        binding.appbarLayout.removeAllViews()
//        binding.appbarLayout.addView(toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//
//    }

}