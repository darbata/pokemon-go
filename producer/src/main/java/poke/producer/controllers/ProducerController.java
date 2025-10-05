package poke.producer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import poke.producer.components.Hasher;
import poke.producer.dto.Coordinates;
import poke.producer.services.KafkaProducerService;

@RestController
public class ProducerController {

    private final KafkaProducerService kafka;

    @Autowired
    private Hasher hasher;

    public ProducerController(KafkaProducerService kafka) {
        this.kafka = kafka;
    }

    @PostMapping("/api/login")
    public void login(@RequestBody Coordinates body) {
        int partition = hasher.hash(body);
        kafka.sendMessageToPartition(partition, body.toMessage());
        System.out.println("produced coordinates");
    }

}