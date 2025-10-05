package poke.producer.components;

import org.springframework.stereotype.Component;

import poke.producer.dto.Coordinates;

@Component
public class Hasher {
    
    private int numPartitions = 4;

    public int hash(Coordinates coords) {
        int y = coords.getY();
        return y % numPartitions;
    }

}
