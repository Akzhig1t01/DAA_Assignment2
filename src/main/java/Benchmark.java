import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                runW1(n, writer);
                runW2(n, writer);
                runW3(n, writer);
                runW4(n, writer);
            }
            System.out.println("Benchmark finished successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runW1(int n, PrintWriter writer) {
        double[] daTimes = new double[RUNS];
        Metrics[] daMetrics = new Metrics[RUNS];
        double[] listTimes = new double[RUNS];
        Metrics[] listMetrics = new Metrics[RUNS];

        for (int r = 0; r < RUNS; r++) {
            Random rng = new Random(42);
            DynamicArray da = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) {
                int val = rng.nextInt();
                da.add(val, null);
                list.add(val, null);
            }

            Metrics mDA = new Metrics();
            long t0 = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                da.get(rng.nextInt(n), mDA);
            }
            long t1 = System.nanoTime();
            daTimes[r] = (t1 - t0) / 1_000_000.0;
            daMetrics[r] = mDA;

            Metrics mList = new Metrics();
            rng = new Random(42);
            for (int i = 0; i < n; i++) rng.nextInt();
            t0 = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                list.get(rng.nextInt(n), mList);
            }
            t1 = System.nanoTime();
            listTimes[r] = (t1 - t0) / 1_000_000.0;
            listMetrics[r] = mList;
        }

        saveMedian(writer, "W1", "none", "DynamicArray", n, daTimes, daMetrics);
        saveMedian(writer, "W1", "none", "MyLinkedList", n, listTimes, listMetrics);
    }

    private static void runW2(int n, PrintWriter writer) {
        double[] daTimes = new double[RUNS];
        Metrics[] daMetrics = new Metrics[RUNS];
        double[] listTimes = new double[RUNS];
        Metrics[] listMetrics = new Metrics[RUNS];

        for (int r = 0; r < RUNS; r++) {
            Random rng = new Random(42);
            DynamicArray da = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            int[] inserted = new int[n];
            for (int i = 0; i < n; i++) {
                inserted[i] = rng.nextInt(1_000_000);
                da.add(inserted[i], null);
                list.add(inserted[i], null);
            }

            int[] queries = new int[1000];
            for (int i = 0; i < 500; i++) queries[i] = inserted[rng.nextInt(n)];
            for (int i = 500; i < 1000; i++) queries[i] = 1_000_000 + rng.nextInt(1_000_000);

            Metrics mDA = new Metrics();
            long t0 = System.nanoTime();
            for (int q : queries) da.contains(q, mDA);
            long t1 = System.nanoTime();
            daTimes[r] = (t1 - t0) / 1_000_000.0;
            daMetrics[r] = mDA;

            Metrics mList = new Metrics();
            t0 = System.nanoTime();
            for (int q : queries) list.contains(q, mList);
            t1 = System.nanoTime();
            listTimes[r] = (t1 - t0) / 1_000_000.0;
            listMetrics[r] = mList;
        }

        saveMedian(writer, "W2", "none", "DynamicArray", n, daTimes, daMetrics);
        saveMedian(writer, "W2", "none", "MyLinkedList", n, listTimes, listMetrics);
    }

    private static void runW3(int n, PrintWriter writer) {
        for (String variant : new String[]{"head", "middle"}) {
            double[] daTimes = new double[RUNS];
            Metrics[] daMetrics = new Metrics[RUNS];
            double[] listTimes = new double[RUNS];
            Metrics[] listMetrics = new Metrics[RUNS];

            for (int r = 0; r < RUNS; r++) {
                Random rng = new Random(42);
                DynamicArray da = new DynamicArray();
                MyLinkedList list = new MyLinkedList();
                for (int i = 0; i < n; i++) {
                    int val = rng.nextInt();
                    da.add(val, null);
                    list.add(val, null);
                }

                int idx = variant.equals("head") ? 0 : da.size() / 2;

                Metrics mDA = new Metrics();
                long t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) da.add(idx, 999, mDA);
                for (int i = 0; i < 1000; i++) da.remove(idx, mDA);
                long t1 = System.nanoTime();
                daTimes[r] = (t1 - t0) / 1_000_000.0;
                daMetrics[r] = mDA;

                Metrics mList = new Metrics();
                t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) list.add(idx, 999, mList);
                for (int i = 0; i < 1000; i++) list.remove(idx, mList);
                t1 = System.nanoTime();
                listTimes[r] = (t1 - t0) / 1_000_000.0;
                listMetrics[r] = mList;
            }

            saveMedian(writer, "W3", variant, "DynamicArray", n, daTimes, daMetrics);
            saveMedian(writer, "W3", variant, "MyLinkedList", n, listTimes, listMetrics);
        }
    }

    private static void runW4(int n, PrintWriter writer) {
        double[] times = new double[RUNS];
        Metrics[] metrics = new Metrics[RUNS];

        for (int r = 0; r < RUNS; r++) {
            Random rng = new Random(42);
            MinHeap heap = new MinHeap(n);
            Metrics m = new Metrics();

            long t0 = System.nanoTime();
            for (int i = 0; i < n; i++) {
                heap.insert(rng.nextInt(), m);
            }
            for (int i = 0; i < n; i++) {
                heap.extractMin(m);
            }
            long t1 = System.nanoTime();

            times[r] = (t1 - t0) / 1_000_000.0;
            metrics[r] = m;
        }

        saveMedian(writer, "W4", "none", "MinHeap", n, times, metrics);
    }

    private static void saveMedian(PrintWriter writer, String wl, String var, String struct, int n, double[] times, Metrics[] metrics) {
        Double[] sortedTimes = new Double[RUNS];
        for (int i = 0; i < RUNS; i++) sortedTimes[i] = times[i];
        Arrays.sort(sortedTimes);

        int medianIdx = RUNS / 2;
        double medTime = sortedTimes[medianIdx];

        Metrics medM = metrics[0];
        for (int i = 0; i < RUNS; i++) {
            if (times[i] == medTime) {
                medM = metrics[i];
                break;
            }
        }

        writer.printf(Locale.US, "%s,%s,%s,%d,%.4f,%d,%d,%d\n", wl, var, struct, n, medTime, medM.steps, medM.moves, medM.comparisons);
    }
}