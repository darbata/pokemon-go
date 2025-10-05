package poke.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import poke.consumer.components.SparseMatrix;


@Service
public class KafkaConsumerService {

    @Autowired
    private SparseMatrix map;

    // on message, increment cell in sparse matrix
    @KafkaListener(topics = "events.login", groupId = "consumer-group")
    public void consumer(String message) throws JsonMappingException, JsonProcessingException { // receive coordinate
        System.out.println("consumed coordinates");
        System.out.println(message); // {"x":755,"y":838}

        // parse string to get x and y dont bother with orm
        String[] parts = message.replaceAll("[^0-9,]", "").split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);

        map.increment(x, y);
    }


}