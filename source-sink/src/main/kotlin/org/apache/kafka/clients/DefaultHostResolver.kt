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
package org.apache.kafka.clients

import java.net.InetAddress
import kotlin.concurrent.Volatile

class DefaultHostResolver: HostResolver {
    companion object {
        @Volatile
        @JvmStatic
        var IP_ADDRESS: String = ""
        @Volatile
        @JvmStatic
        var KAFKA_SERVER_NAME: String = ""
        @Volatile
        @JvmStatic
        var KAFKA_SERVICE_NAME: String = ""
    }

    override fun resolve(host: String?): Array<InetAddress> {
        var result: Array<InetAddress> = arrayOf()
        if (host!!.startsWith(KAFKA_SERVER_NAME) && !IP_ADDRESS.isBlank()) {
            val addressArr = arrayOf<InetAddress>(InetAddress.getByAddress(host, InetAddress.getByName(IP_ADDRESS).address))
            result = addressArr
        } else if (host.startsWith(KAFKA_SERVER_NAME) && !KAFKA_SERVICE_NAME.isBlank()) {
            result = InetAddress.getAllByName(KAFKA_SERVICE_NAME)
        }
        return result
    }

}