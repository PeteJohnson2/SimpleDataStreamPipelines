/**
 * Copyright 2023 Sven Loesekann
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.xxx.dbtorest.sink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.xxx.dbtorest.model.DbChangeDto;
import de.xxx.dbtorest.sink.model.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;


@Service
public class DbChangeSinkService {
    private static final Logger LOG = LoggerFactory.getLogger(DbChangeSinkService.class);
    private final JsonMapper objectMapper;
    private final RestClient restClient = RestClient.create();
    @Value("${rest.endpoint.url}")
    private String restEndpointUrl;

    public DbChangeSinkService(JsonMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleDbChange(DbChangeDto dbChangeDto) {
        var wrapper = Optional.ofNullable( dbChangeDto.value()).stream().map(value -> this.objectMapper.readValue(value, Wrapper.class)).findFirst().orElse(new Wrapper(null, null));
        LOG.info("DbChange received: {}", dbChangeDto.toString());
        this.restClient.post().uri(this.restEndpointUrl.trim() + "/rest/orderproduct").contentType(MediaType.APPLICATION_JSON).body(wrapper).retrieve().toBodilessEntity();
    }
}
