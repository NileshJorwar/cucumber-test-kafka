package gadaptertest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;


public class CucumberKafkaProducer {
    private final Properties properties = getProperties();

    public CucumberKafkaProducer() {
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.51.4.10:30131");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        return properties;
    }

    public void sendMessage( String topicName, String message )
    {
        String partitionKey = UUID.randomUUID().toString();
        try (KafkaProducer<Object, Object> kafkaProducerLocal = new KafkaProducer<>(properties)) {
            kafkaProducerLocal.send(new ProducerRecord<>(topicName, partitionKey, message));
        }
    }
}
