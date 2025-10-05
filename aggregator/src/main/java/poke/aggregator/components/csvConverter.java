package poke.aggregator.components;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class csvConverter {

    public Path mapToCSV(int[][] map, String filename) throws IOException {
        Path out = Paths.get("/tmp/out");
        Files.createDirectories(out);
        Path tmp = out.resolve("heatmap.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(tmp)) {
            for (int[] row : map) {
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) { // avoid leading comma
                        writer.write(',');
                    }
                    writer.write(Integer.toString(row[i]));
                }
                writer.newLine();
            }
        }
        return tmp;
    }
    
}
