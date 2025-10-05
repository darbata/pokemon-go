package poke.consumer.components;

import org.springframework.stereotype.Component;

@Component
public class HeatMap {
    private int[][] map;

    public HeatMap() {
        this.map = new int[100][100];
    }

    public void increment(int row, int col) {
        map[row][col] += 1;
    }

    public void clear() {
        this.map = new int[100][100];
    }

    public int[][] getMap() {
        return map;
    }
}
