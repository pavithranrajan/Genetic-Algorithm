import java.io.FileNotFoundException;
import java.io.IOException;

public interface Algorithm {
    Result runAlgorithm(int sampleSize, int populationSize, String solution) throws IOException;
}
