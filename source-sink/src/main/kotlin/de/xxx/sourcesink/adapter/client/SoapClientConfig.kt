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

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller


@Configuration
class SoapClientConfig {
    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val marshaller = Jaxb2Marshaller()
        marshaller.contextPath = "com.baeldung.springsoap.client.gen"
        return marshaller
    }

    @Bean
    fun soapClient(marshaller: Jaxb2Marshaller?): SoapClient {
        val client = SoapClient()
        client.setDefaultUri("http://localhost:8080/ws")
        client.setMarshaller(marshaller)
        client.setUnmarshaller(marshaller)
        return client
    }
}