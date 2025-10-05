package poke.consumer.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import poke.consumer.dto.Cell;

@Component
public class SparseMatrix {
    private final Map<Cell, Integer> map = new HashMap<>();

    public SparseMatrix() {
        map.put(new Cell(0, 0), 20);
        map.put(new Cell(100, 100), 30);
        map.put(new Cell(50, 50), 10);
        map.put(new Cell(75, 75), 5);
        map.put(new Cell(25, 25), 15);
        map.put(new Cell(10, 90), 8);
        map.put(new Cell(90, 10), 12);
    }

    public void increment(int row, int col) {
        Cell cell = new Cell(row, col); 
        map.put(cell, map.getOrDefault(cell, 0) + 1);
    }

    public void clear() {
        map.clear();
    }

    public Map<Cell, Integer> getMap() {
        return map;
    }
    
}
