package com.example.model

data class AiModel(
    val id: String,
    val name: String,
    val emoji: String,
    val category: String,
    val whatItDoes: String,
    val bestFor: List<String>,
    val pros: List<String>,
    val cons: List<String>,
    val detailsAndCreator: String,
    val webLink: String
)
