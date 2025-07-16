package org.colman.travelie.features.destinations

import org.colman.travelie.domain.Destinations.GetDestinations

data class DestinationsUseCases(
    val getDestinations: GetDestinations,
)