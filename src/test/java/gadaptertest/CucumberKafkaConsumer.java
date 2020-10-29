package gadaptertest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CucumberKafkaConsumer {
    private static final int MAX_ALLOWED_LATENCY = 5000;

    private final Properties properties = getProperties();

    private final String topic;

    public CucumberKafkaConsumer(String topic) {
        this.topic = topic;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.51.4.10:30131");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"df-fabric-pro-FABRIC-PRO-GROUP");
        return properties;
    }

    public Map<String, Integer> consumeMessage() {
        Map<String, Integer> records = new HashMap<>();

        try (KafkaConsumer<Object,Object> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(Collections.singletonList(topic));

            long endPollingTimestamp = System.currentTimeMillis() + MAX_ALLOWED_LATENCY;

            while (System.currentTimeMillis() < endPollingTimestamp) {
                ConsumerRecords<Object, Object> consumerRecords = kafkaConsumer.poll(100);
                for (ConsumerRecord<Object, Object> next : consumerRecords) {
                    /*String[] splitted = next.value().split(":");
                    records.put(splitted[0], Integer.valueOf(splitted[1]));*/
                }
            }

        }

        return records;
    }
}
