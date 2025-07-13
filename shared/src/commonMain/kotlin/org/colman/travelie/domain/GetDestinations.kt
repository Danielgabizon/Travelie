package org.colman.travelie.domain

import org.colman.travelie.data.destinations.DestinationsRepository


class GetDestinations (
    private val destinationsRepository: DestinationsRepository
){
    suspend operator fun invoke() = destinationsRepository.getDestinations()

}