package poke.aggregator.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import poke.aggregator.components.HeatMap;
import poke.aggregator.components.S3;
import poke.aggregator.components.csvConverter;
import poke.aggregator.dtos.CellCount;
import poke.aggregator.dtos.SparseMatrixMessage;


@Service
public class KafkaConsumerService {

    @Autowired  
    S3 s3;  

    @Autowired
    csvConverter csvConverter;

    @Autowired
    HeatMap map; 

    private volatile String currentKey = null;

    @Async
    private void asyncIncrement(List<CellCount> cells) {
        for (var cell : cells) { // update heat map
            map.increment(cell.row(), cell.col(), cell.count());
        }
    }

    @Async void asyncStash() throws IOException {
        var copy = map.snapshotAndClear();
        var path = csvConverter.mapToCSV(copy, "heatmap.csv");
        s3.uploadFile(path);
    }

    // on message, increment cell in sparse matrix
    @KafkaListener(topics = "matrices", groupId = "aggregator-group")
    public void consumer(
        @Header(KafkaHeaders.RECEIVED_KEY) String key, 
        @Payload SparseMatrixMessage message) throws IOException { // receive coordinate

        if (!Objects.equals(currentKey, key)) {
            System.out.println("New key detected, exporting current map to S3");

            asyncStash();

            currentKey = key;

            System.out.println("Key updated to: " + currentKey);
        }

        System.out.println("key: " + key + " consumed matrix");
        List<CellCount> cells = message.cells(); 
        asyncIncrement(cells); // run increment in separate thread
    }
}