package poke.aggregator.components;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

@Component
public class HeatMap {
    private int[][] map = new int[100][100];
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void increment(int row, int col, int count) {
        lock.writeLock().lock();
        try {
            map[row][col] += count;
            System.out.println("Incremented cell (" + row + ", " + col + ") by " + count);
        } finally {
            System.out.println("(0, 0): " + map[0][0]);
            System.out.println("(0, 1): " + map[0][1]);
            System.out.println("(1, 0): " + map[1][0]);
            System.out.println("(1, 1): " + map[1][1]);
            lock.writeLock().unlock();
        }
    }

    public int[][] snapshotAndClear() { // get and clear
        lock.writeLock().lock();
        try {
            int[][] copy = new int[100][100];
            for (int i = 0; i < 100; i++) {
                System.arraycopy(map[i], 0, copy[i], 0, 100);
            }
            map = new int[100][100];
            return copy;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
