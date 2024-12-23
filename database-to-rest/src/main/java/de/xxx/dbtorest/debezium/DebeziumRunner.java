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
package de.xxx.dbtorest.debezium;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PreDestroy;

@Component
public class DebeziumRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(DebeziumRunner.class);
    @Value("${customer.datasource.host}")
    private String customerDbHost;

    @Value("${customer.datasource.database}")
    private String customerDbName;

    @Value("${customer.datasource.port}")
    private String customerDbPort;

    @Value("${customer.datasource.username}")
    private String customerDbUsername;

    @Value("${customer.datasource.password}")
    private String customerDbPassword;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @EventListener
    public void onEvent(ApplicationReadyEvent event) {
    	final Properties props = new Properties();
    	props.setProperty("name", "engine");
    	props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
    	props.setProperty("database.hostname", this.customerDbHost);
    	props.setProperty("database.port", this.customerDbPort);
    	props.setProperty("database.user", this.customerDbUsername);
    	props.setProperty("database.password", this.customerDbPassword);
    	props.setProperty("database.dbname", "postgres");
    	props.setProperty("topic.prefix", "stream");
    	props.setProperty("table.include.list", "public.customer");
    	props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    	props.setProperty("offset.storage.file.filename", "./offsets.dat");
    	props.setProperty("offset.flush.interval.ms", "60000");
    	
    	try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
    	        .using(props)
    	        .notifying(myRecord -> {
    	            LOGGER.info(myRecord.toString());
    	        })
    	        .build()) {
    		this.executor.execute(engine);
    	} catch (IOException e) {    		
    		throw new RuntimeException(e);
		}
    	
    	
    }
    	
    	@PreDestroy
    	public void onShutdown() {
    		try {
    		    executor.shutdown();
    		    while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
    		        LOGGER.info("Waiting another 5 seconds for the embedded engine to shut down");
    		    }
    		}
    		catch ( InterruptedException e ) {
    		    Thread.currentThread().interrupt();
    		}
    	}
    
}
