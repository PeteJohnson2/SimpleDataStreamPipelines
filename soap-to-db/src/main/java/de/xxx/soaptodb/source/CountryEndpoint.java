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
package de.xxx.soaptodb.source;

import com.baeldung.springsoap.gen.GetCountryRequest;
import com.baeldung.springsoap.gen.GetCountryResponse;
import de.xxx.soaptodb.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {
    private static final Logger log = LoggerFactory.getLogger(CountryEndpoint.class);
    private static final String NAMESPACE_URI = "http://www.baeldung.com/springsoap/gen";
    private final CountrySourceService countryService;

    public CountryEndpoint(CountrySourceService countryService, KafkaProducer kafkaProducer) {
        this.countryService = countryService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        //log.info("Country: {}", request.getName());
        GetCountryResponse response = new GetCountryResponse();
        var country = countryService.sendCountry(request.getName());
        response.setCountry(country);
        return response;
    }
}
