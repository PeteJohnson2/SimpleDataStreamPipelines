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
package de.xxx.soaptodb.model;

import com.baeldung.springsoap.gen.Country;

import java.util.UUID;

public class CountryDto extends Country {
    private UUID id = UUID.randomUUID();

    public CountryDto(Country country) {
        this.setCapital(country.getCapital());
        this.setCurrency(country.getCurrency());
        this.setName(country.getName());
        this.setPopulation(country.getPopulation());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CountryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", capital='" + capital + '\'' +
                ", currency=" + currency +
                '}';
    }
}
