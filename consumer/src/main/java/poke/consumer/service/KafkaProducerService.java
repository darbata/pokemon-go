package poke.consumer.service;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import poke.consumer.components.SparseMatrix;
import poke.consumer.dto.CellCount;

@Service
public class KafkaProducerService {

    @Autowired
    SparseMatrix matrix;

    private static final String TOPIC = "matrices";
    private static int messageId = 0;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public <T> CompletableFuture<SendResult<String, Object>> sendMessage (T message) {

        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, String.valueOf(messageId), message);

        System.out.println("Sending message: " + message + " with ID: " + messageId);

        messageId++;
        return kafkaTemplate.send(record);
    }

    // @Scheduled(fixedRate = 900000) // 15 minutes
    @Scheduled(fixedRate = 30000) // 30 seconds for testing
    private void stash() {

        var cells = matrix.getMap()
            .entrySet()
            .stream()
            .map(pair -> {
                var coords = pair.getKey();
                var count = pair.getValue();
                return new CellCount(coords.row(), coords.col(), count);
            })
            .toList();
        
        var sparseMatrixMessage = new poke.consumer.dto.SparseMatrixMessage(cells);

        sendMessage(sparseMatrixMessage);

        matrix.clear();
        System.out.println("produced sparse matrix");
    }

}