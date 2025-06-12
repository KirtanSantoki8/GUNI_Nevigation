package com.devkt.guninevigation.model

data class SpecificLocationResponse(
    val message: List<LocationItems>,
    val status: Int
)

data class LocationItems(
    val date: String,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val latitude: String,
    val longitude: String,
    val mainLocation: String,
    val name: String,
    val phone_no: String,
    val uid: String
)