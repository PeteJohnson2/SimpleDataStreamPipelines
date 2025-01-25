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
package de.xxx.eventtofile.sink;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.xxx.eventtofile.model.FlightDto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

@Transactional
@Service
public class FlightSinkService {
    private static final Logger log = LoggerFactory.getLogger(FlightSinkService.class);
    private final ObjectMapper objectMapper;

    public FlightSinkService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        var tempDir = new File(System.getProperty("java.io.tmpdir"));
        var filesToDelete = tempDir.listFiles(((dir, name) -> name.matches("Flight-.+")));
        Stream.of(filesToDelete).forEach(File::delete);
        log.info("Deleted {} files.", filesToDelete.length);
    }

    public void handleFlightEvent(FlightDto flightDto) {
        var tempDirectory = System.getProperty("java.io.tmpdir")+File.separator;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempDirectory+"Flight-"+flightDto.id().toString()));
            writer.write(this.objectMapper.writeValueAsString(flightDto));
            writer.close();
            log.info("File written: {}", tempDirectory+"Flight-"+flightDto.id().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
