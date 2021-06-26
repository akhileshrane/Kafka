package com.kafka.beginners;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallbackDemo {

    public static void main(String[] args) {
        String bootstrapServer = "127.0.0.1:9092";
        //create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG,"1");
        //create producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for(int i = 0 ; i <10 ; i++) {
            //create records
            ProducerRecord<String, String> record = new ProducerRecord<>("java-topic", "hello world!"+i);

            //send data --async
            producer.send(record, (recordMetadata, e) -> {
                if (e == null)
                    System.out.println(recordMetadata.topic() + "\n" + recordMetadata.offset() + "\n" + recordMetadata.timestamp() + "\n" + recordMetadata.partition());
                else {
                    System.out.println("F");
                    e.printStackTrace();
                }
            });
        }

        //blocking
        producer.flush();
        producer.close();
    }
}
