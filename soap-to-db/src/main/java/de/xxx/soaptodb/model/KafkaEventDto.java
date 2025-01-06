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

public class KafkaEventDto {
    private String topicName;
    private String topicContent;

    public KafkaEventDto() {
    }

    public KafkaEventDto(String topicName, String topicContent) {
        super();
        this.topicName = topicName;
        this.topicContent = topicContent;
    }

    public String getTopicName() {
        return topicName;
    }
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    public String getTopicContent() {
        return topicContent;
    }
    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    @Override
    public String toString() {
        return "KafkaEventDto [topicName=" + topicName + ", topicContent=" + topicContent + "]";
    }
}
