package org.colman.travelie.models


data class Destination(
    val title: String,
    val description: String,
    val link: String,
    val flightPrice: Int,
    val hotelPrice: Int,
    val thumbnail: String,
)