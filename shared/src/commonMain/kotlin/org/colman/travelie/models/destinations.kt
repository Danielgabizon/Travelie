package org.colman.travelie.models

import kotlinx.serialization.Serializable

@Serializable
data class Destinations (
    val items: List<Destination>
)