package org.ricky.disruptortechsharingcodeexamples.benchmark.latency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.ricky.disruptortechsharingcodeexamples.benchmark.latency.Const.*;
import static org.ricky.disruptortechsharingcodeexamples.benchmark.latency.LatencyStats.analyzeLatencies;

public class ArrayBlockingQueueLatencyBenchmark {
  private static final List<Long> latencies = Arrays.asList(new Long[OPERATIONS]);
  public static void main(String[] args) throws Exception {
    for (int i = 0; i < WARMUP_ITERATIONS; i++) {
      runBenchMark();
    }
    runBenchMark();
    analyzeLatencies(latencies);
  }

  private static void runBenchMark() throws InterruptedException {
    BlockingQueue<Long> queue = new ArrayBlockingQueue<>(BUFFER_SIZE);

    Thread consumer = new Thread(() -> {
      try {
        for (int i = 0; i < OPERATIONS; i++) {
          long startTime = queue.take();
          long endTime = System.nanoTime();
          latencies.set(i, endTime - startTime);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    consumer.start();
    for (int i = 0; i < OPERATIONS; i++) {
      queue.put(System.nanoTime());
    }
    consumer.join();
  }
}
