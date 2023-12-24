package com.example.androidlab3

data class NewsItem(
    val status: String?,
    val totalResults: Int?,
    val results: List<Article>?
)

data class Article(
    val article_id: String?,
    val title: String?,
    val link: String?,
    val description: String?,
)
