package org.colman.travelie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Destination(
    val title: String,
    val description: String,
    val link: String,
    @SerialName("extracted_flight_price") val flightPrice: Int?=null,
    @SerialName("extracted_hotel_price") val hotelPrice: Int?= null,
    val thumbnail: String,
)