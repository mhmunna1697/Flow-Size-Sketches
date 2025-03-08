import java.io.*;
import java.util.*;
import java.util.Random;

public class CountMinSketch {
    public static final int D = 3; // Number of counter arrays
    public static final int W = 3000; // Number of counters in each array
    public static final int TOP_K = 100; // Number of largest flows to display
    public int[][] counters = new int [D][W];
    public int[] hashSeeds = new int [D];
    public Map<String, Integer> trueSizes = new HashMap<>();  // hashmap to store top_k flows
    public static Random rand = new Random();

    public CountMinSketch() {
        for (int i = 0; i < D; i++) {
            hashSeeds[i] = rand.nextInt(Integer.MAX_VALUE);
        }
    }

    public int hash(String flowID, int i) {
        int hash = flowID.hashCode() ^ hashSeeds[i];
        hash ^= (hash >>> 16);
        return Math.abs(hash % W);
    }

    public void recordFlow(String flowID, int size) {
        trueSizes.put(flowID, trueSizes.getOrDefault(flowID, 0) + size);
        for (int i = 0; i < D; i++) {
            counters[i][hash(flowID, i)] += size;
        }
    }

    public int estimateFlowSize(String flowID) {
        int minEstimate = Integer.MAX_VALUE;
        for (int i = 0; i < D; i++) {
            minEstimate = Math.min(minEstimate, counters[i][hash(flowID, i)]);
        }
        return minEstimate;
    }

    public void processFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 2) continue;
                String flowID = parts[0];
                int size = Integer.parseInt(parts[1]);
                recordFlow(flowID, size);
            }
        }
    }

    public void generateOutput(String outputFilename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            double totalError = 0;
            List<String[]> flowErrors = new ArrayList<>();
            
            for (Map.Entry<String, Integer> entry : trueSizes.entrySet()) {
                String flowID = entry.getKey();
                int trueSize = entry.getValue();
                int estimatedSize = estimateFlowSize(flowID);
                int error = Math.abs(estimatedSize - trueSize);
                totalError += error;
                flowErrors.add(new String[]{flowID, String.valueOf(estimatedSize), String.valueOf(trueSize)});
            }
            
            double avgError = totalError / trueSizes.size();
            writer.write(String.format("Average Error: "+"%.2f\n", avgError));
            
            flowErrors.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));
            for (int i = 0; i < Math.min(TOP_K, flowErrors.size()); i++) {
                writer.write(flowErrors.get(i)[0] + " " + flowErrors.get(i)[1] + " " + flowErrors.get(i)[2] + "\n");
            }
        }
    }

    public static void main(String[] args) {
        CountMinSketch cms = new CountMinSketch();
        try {
            cms.processFile("project2input.txt");
            cms.generateOutput("countMin_output.txt");
            System.out.println("Output written to countMin_output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
