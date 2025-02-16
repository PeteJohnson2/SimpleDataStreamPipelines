/**
 *    Copyright 2023 Sven Loesekann
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package de.xxx.sourcesink.usecase.service

import de.xxx.sourcesink.adapter.event.KafkaProducer
import de.xxx.sourcesink.domain.model.FlightDto
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@Service
class FlightService(val kafkaProducer: KafkaProducer) {
    val destinations = listOf("Singapore", "Sydney", "Tokio", "Shanghai", "Mumbai", "Beijing", "Bangkok", "Perth", "Taipeh")
    val airlines = listOf("Qantas", "Singapore Airlines", "AirAsia", "China Southern Airlines", "ANA", "Air India")

    fun sendFlight() {
        this.kafkaProducer.sendFlightMsg(this.createFlight())
    }

    private fun createFlight(): FlightDto {
        val from = destinations.get(Random.nextInt(9))
        val to = destinations.filter { it != from }.get(Random.nextInt(8))
        return FlightDto(UUID.randomUUID(), from, to, 180L + Random.nextInt(180), airlines.get(Random.nextInt(6)), BigDecimal(
            200 + Random.nextInt(400)))
    }
}