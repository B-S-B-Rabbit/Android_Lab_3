package com.example.androidlab3

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.androidlab3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = newsAdapter
        binding.searchButton.setOnClickListener {
            val keyword = binding.keywordEditText.text.toString().trim()
            if (keyword.isNotEmpty()) {
                fetchNewsByKeyword(keyword)
            } else {
                Toast.makeText(this, "Enter a keyword", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun handleNewsResponse(newsItem: NewsItem?) {
        if (newsItem == null) {
            Toast.makeText(this@MainActivity, "No news found", Toast.LENGTH_SHORT).show()
        } else {
            val articles = newsItem.results
            if (articles.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, "No articles found", Toast.LENGTH_SHORT).show()
            } else {
                newsAdapter.submitList(articles.toMutableList()) {
                    binding.recyclerView.post {
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                        binding.recyclerView.smoothScrollToPosition(0)
                        binding.recyclerView.smoothScrollToPosition(newsAdapter.itemCount - 1)
                        binding.recyclerView.smoothScrollToPosition(0)
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun fetchNewsByKeyword(keyword: String) {
        val call: Call<NewsItem> = NewsApiClient.newsApi.getNews(keyword, "pub_35354de2c61f9d8b3153cdd793f13a7a81b68")

        binding.progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<NewsItem> {
            override fun onResponse(call: Call<NewsItem>, response: Response<NewsItem>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val newsItem = response.body()
                    handleNewsResponse(newsItem)
                } else {
                    Log.e("MainActivity", "Failed to load news: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsItem>, t: Throwable) {
                binding.progressBar.visibility = View.GONE

                Log.e("MainActivity", "Network error: ${t.message}")
            }
        })
    }
}

