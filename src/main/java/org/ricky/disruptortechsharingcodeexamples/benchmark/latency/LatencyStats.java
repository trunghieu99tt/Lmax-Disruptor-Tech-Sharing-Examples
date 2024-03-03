package org.ricky.disruptortechsharingcodeexamples.benchmark.latency;

import java.util.Collections;
import java.util.List;

public class LatencyStats {

    public static void analyzeLatencies(List<Long> latencies) {
      Collections.sort(latencies);

      long minLatency = latencies.get(0);
      long maxLatency = latencies.get(latencies.size() - 1);
      long sumLatency = 0;
      for (long latency : latencies) {
        sumLatency += latency;
      }
      double meanLatency = sumLatency / (double) latencies.size();

      // Percentiles
      long p95Latency = latencies.get((int) (latencies.size() * 0.95) - 1);
      long p99Latency = latencies.get((int) (latencies.size() * 0.99) - 1);

      System.out.println("Min Latency: " + minLatency + " ns");
      System.out.println("Max Latency: " + maxLatency + " ns");
      System.out.println("Mean Latency: " + meanLatency + " ns");
      System.out.println("P95 Latency: " + p95Latency + " ns");
      System.out.println("P99 Latency: " + p99Latency + " ns");
  }
}
