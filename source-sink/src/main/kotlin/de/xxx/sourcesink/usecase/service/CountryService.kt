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

import de.xxx.sourcesink.domain.client.SoapClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CountryService(val soapClient: SoapClient) {
    val log = LoggerFactory.getLogger(CountryService::class.java)
    val countries = listOf("Spain","Poland","United Kingdom")
    var index = 0

    fun generateSoapData() {
        val response = this.soapClient.getCountry(countries.get(this.index))
        log.info("Country: {}", response.country.name)
        index = if(index >= 2) 0 else index + 1
    }
}