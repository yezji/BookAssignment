package com.yeji.bookassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

    private var backButtonTime = 0L

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


        initUI()

    }

    private fun initUI() {
        viewModel.keyword.observe(this) { keyword ->
            Log.d("yezzz mainactivity", "keyword: $keyword")
            viewModel.getAllList()
        }

        viewModel.isProgressVisible.observe(this) { isVisible ->
            // set progressBar visibility
            binding.progressBarMain.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        viewModel.loadError.observe(this) { errorMsg ->
            if (!errorMsg.equals("")) Toast.makeText(this, "err: $errorMsg", Toast.LENGTH_SHORT).show()
        }

    }



    /*override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val gapTime = currentTime - backButtonTime

        if (gapTime in 0..2000) {
            // 2초 안에 뒤로가기를 두번 누를 시 앱 종료
            finish()
            finish()
        }
        else {
            backButtonTime = currentTime
            if (supportFragmentManager.backStackEntryCount <= 1) Toast.makeText(this, getString(R.string.search_main_backclosetoast), Toast.LENGTH_SHORT).show()
            else super.onBackPressed()
        }
    }*/

}
