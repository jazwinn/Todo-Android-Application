package com.example.weatherapi

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapi.databinding.ActivityMainBinding
import okio.IOException
import retrofit2.HttpException

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycleView()

        lifecycleScope.launchWhenCreated {
            binding.ProgressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            }
            catch (e: IOException){
                Log.e(TAG, "IOException, you might not have internet connection")
                binding.ProgressBar.isVisible = false
                return@launchWhenCreated
            }
            catch (e: HttpException){
                Log.e(TAG, "HttpException, unexpected response")
                binding.ProgressBar.isVisible = false
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null){
                todoAdapter.todos = response.body()!!
            }
            else{
                Log.e(TAG, "Response not successful")
            }
            binding.ProgressBar.isVisible = false;
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

    }

    private fun setupRecycleView() = binding.rvTodo.apply {
        todoAdapter = TodoAdapter()
        adapter = todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}