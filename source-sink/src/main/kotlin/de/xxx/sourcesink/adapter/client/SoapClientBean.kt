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
package de.xxx.sourcesink.adapter.client

import com.baeldung.springsoap.client.gen.GetCountryRequest
import com.baeldung.springsoap.client.gen.GetCountryResponse
import de.xxx.sourcesink.domain.client.SoapClient
import org.springframework.stereotype.Component
import org.springframework.ws.client.core.support.WebServiceGatewaySupport

@Component
class SoapClientBean(): WebServiceGatewaySupport(), SoapClient {
    override fun getCountry(country: String): GetCountryResponse {
        val request: GetCountryRequest = GetCountryRequest()
        request.setName(country)

        val response: GetCountryResponse = webServiceTemplate.marshalSendAndReceive(request) as GetCountryResponse

        return response
    }
}