package org.ricky.disruptortechsharingcodeexamples.mutualexclusive;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@State(Scope.Benchmark)
public class LockAndCasBenchmark {

  private long counter = 0;
  private final Lock lock = new ReentrantLock();
  private final AtomicLong atomicCounter = new AtomicLong();

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void singleThreadIncrement() {
    counter++;
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void singleThreadIncrementWithLock() {
    lock.lock();
    try {
      counter++;
    } finally {
      lock.unlock();
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(2)
  public void twoThreadsIncrementWithLock() {
    lock.lock();
    try {
      counter++;
    } finally {
      lock.unlock();
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void singleThreadIncrementWithCAS() {
    atomicCounter.incrementAndGet();
  }

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @Threads(2)
  public void twoThreadsIncrementWithCAS() {
    atomicCounter.incrementAndGet();
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(LockAndCasBenchmark.class.getSimpleName())
        .forks(1)
        .build();

    new Runner(opt).run();
  }
}
