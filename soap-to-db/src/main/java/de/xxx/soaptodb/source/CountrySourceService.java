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

import com.baeldung.springsoap.gen.Country;
import com.baeldung.springsoap.gen.WsCurrency;
import de.xxx.soaptodb.kafka.KafkaProducer;
import de.xxx.soaptodb.model.CountryDto;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CountrySourceService {
    private static final Logger log = LoggerFactory.getLogger(CountrySourceService.class);
    private static final Map<String, Country> countries = new HashMap<>();
    private final KafkaProducer kafkaSender;

    public CountrySourceService(KafkaProducer kafkaClient) {
        this.kafkaSender = kafkaClient;
    }

    @PostConstruct
    public void initData() {
        Country spain = new Country();
        spain.setName("Spain");
        spain.setCapital("Madrid");
        spain.setCurrency(WsCurrency.EUR);
        spain.setPopulation(46704314);

        countries.put(spain.getName(), spain);

        Country poland = new Country();
        poland.setName("Poland");
        poland.setCapital("Warsaw");
        poland.setCurrency(WsCurrency.PLN);
        poland.setPopulation(38186860);

        countries.put(poland.getName(), poland);

        Country uk = new Country();
        uk.setName("United Kingdom");
        uk.setCapital("London");
        uk.setCurrency(WsCurrency.GBP);
        uk.setPopulation(63705000);

        countries.put(uk.getName(), uk);
    }

    public Country sendCountry(String name) {
        Assert.notNull(name, "The country's name must not be null");
        var country = countries.get(name);
        var myCountry = new CountryDto(country);
        this.kafkaSender.sendCountryMsg(myCountry);
        return country;
    }
}
