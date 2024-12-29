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

import de.xxx.sourcesink.domain.entity.OrderProduct
import de.xxx.sourcesink.domain.entity.OrderRepository
import de.xxx.sourcesink.domain.entity.OrderState
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.random.Random

@Transactional
@Service
class OrderService(val orderRepository: OrderRepository) {
    val logger = LoggerFactory.getLogger(OrderService::class.java)
    val orderStates = listOf(OrderState.OPEN, OrderState.PAYED, OrderState.SEND)
    val productNames = listOf("Laptop", "Monitor", "Keyboard", "Mouse")

    fun generateOrder() {
        val orders = this.orderRepository.findAll();
        //logger.info(""+orders.size)
        if (orders.size < 4) {
            val newOrders = productNames.map { productName ->
                val result = Optional.ofNullable(orders.firstOrNull { it.productName == productName }).orElse(
                    OrderProduct(null, productName, Random.nextInt(7) + 1, OrderState.OPEN)
                )
                //logger.info(""+result.orderState)
                result
            }
            logger.info(newOrders.get(0).toString())
            this.orderRepository.saveAll(newOrders.toMutableList())
        } else if (orders.none { it.orderState == OrderState.SEND }) {
            val newOrders = orders.map {
                it.orderState = when (it.orderState) {
                    OrderState.OPEN -> OrderState.PAYED
                    OrderState.PAYED -> OrderState.SEND
                    else -> OrderState.SEND
                }
                //logger.info(""+it.orderState)
                it
            }
            logger.info(newOrders.get(0).toString())
            this.orderRepository.saveAll(newOrders.toMutableList())
        } else {
            logger.info("Delete orders.")
            this.orderRepository.deleteAll()
        }
    }
}