package org.ricky.disruptortechsharingcodeexamples.benchmark.latency;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import static org.ricky.disruptortechsharingcodeexamples.benchmark.latency.Const.*;
import static org.ricky.disruptortechsharingcodeexamples.benchmark.latency.LatencyStats.analyzeLatencies;

class LongEvent {
  long value;
  public void set(long value) { this.value = value; }
}
public class DisruptorLatencyBenchmark {
  private static final List<Long> latencies = Arrays.asList(new Long[OPERATIONS]);

  public static void main(String[] args) {
    // Initialize Disruptor
    Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, BUFFER_SIZE, Executors.defaultThreadFactory());
    disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
      long endTime = System.nanoTime();
      long latency = endTime - event.value;
      latencies.set((int) sequence % OPERATIONS, latency);
    });
    disruptor.start();
    for (int i = 0; i < WARMUP_ITERATIONS; i++) {
      runBenchMark(disruptor);
    }
    runBenchMark(disruptor);
    disruptor.shutdown();
    analyzeLatencies(latencies);
  }

  private static void runBenchMark(Disruptor<LongEvent> disruptor) {
    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
    for (int i = 0; i < OPERATIONS; i++) {
      long sequence = ringBuffer.next();
      try {
        LongEvent event = ringBuffer.get(sequence);
        event.value = System.nanoTime();
      } finally {
        ringBuffer.publish(sequence);
      }
    }
  }
}
