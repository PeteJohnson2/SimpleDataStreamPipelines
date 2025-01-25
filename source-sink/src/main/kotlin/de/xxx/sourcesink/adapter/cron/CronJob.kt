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
package de.xxx.sourcesink.adapter.cron

import de.xxx.sourcesink.usecase.service.CountryService
import de.xxx.sourcesink.usecase.service.FlightService
import de.xxx.sourcesink.usecase.service.OrderService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class CronJob(val orderService: OrderService, val countryService: CountryService, val flightService: FlightService) {
    @Scheduled(fixedDelay = 2000)
    fun generateOrders() {
        this.orderService.generateOrder()
    }
    @Scheduled(fixedDelay = 2000, initialDelay = 500)
    fun generateSoapRequests() {
        this.countryService.generateSoapData()
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 1000)
    fun generateEvents() {
        this.flightService.sendFlight()
    }
}