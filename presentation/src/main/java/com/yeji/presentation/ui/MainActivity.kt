package com.yeji.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.yeji.presentation.databinding.ActivityMainBinding
import com.yeji.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)
    private val viewModel by viewModels<MainViewModel>()
   // private lateinit var viewModel: MainViewModel

    private lateinit var transaction: FragmentTransaction
    private lateinit var searchMainFragment: SearchMainFragment

//    private var backButtonTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         * comments
         * TODO: DI
         * - DI 주입하는 경우 사용
         *   현재는 따로 사용하지 않기에 viewModel by viewModels<MainViewModel>()로 만듦
         */
//        viewModel = ViewModelProvider(this, MainViewModelFactory(repository = (application as App).repository))
//            .get(MainViewModel::class.java)



        // prepare fragment instance
        searchMainFragment = SearchMainFragment()
        // set transaction of sfm
        transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(binding.flMainContent.id, searchMainFragment, null)
            .commit()


        initUI()

    }

    private fun initUI() {
        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
                .collect {
                    showProgressBar()
                    showErrorToast()
                }
        }
    }


    fun showProgressBar(isVisible: Boolean = viewModel.uiState.value.isProgressVisible){
        binding.progressBarMain.isVisible = isVisible
    }
    fun showErrorToast(errorMsg: String? = viewModel.uiState.value.loadError) {
        if (errorMsg.isNullOrBlank()) Toast.makeText(this@MainActivity, "err: $errorMsg", Toast.LENGTH_SHORT).show()
    }


}
