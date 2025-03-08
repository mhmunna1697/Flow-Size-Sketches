import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ActiveCounter {
    private int counter; // 16-bit counter
    private int exponent; // 16-bit exponent
    public static final int COUNTER_BITS = 16;
    public static final int MAX_COUNTER = (1 << COUNTER_BITS) - 1;
    public Random rand = new Random();;

    public ActiveCounter() {
        this.counter = 0;
        this.exponent = 0;
    }

    public void increment() {
        double probability = 1.0 / (1 << exponent); // Probability = 1 / 2^e

        if (rand.nextDouble() < probability) { 
            if (counter < MAX_COUNTER) {
                counter++; // Normal increment
            } else { 
                counter >>= 1; // Halve counter
                exponent++; // Increase exponent
            }
        }
    }

    public double getEstimatedValue() {
        return counter * Math.pow(2, exponent);
    }

    public static void main(String[] args) {
        ActiveCounter ac = new ActiveCounter();
        int iterations = 1_000_000;
        for (int i = 0; i < iterations; i++) {
            ac.increment();
        }
        double finalValue = ac.getEstimatedValue();
        
        try (FileWriter writer = new FileWriter("activeCounter_output.txt")) {
            writer.write("Final value of the Active Counter: " + finalValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
