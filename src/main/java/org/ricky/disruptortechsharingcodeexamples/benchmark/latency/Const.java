package org.ricky.disruptortechsharingcodeexamples.benchmark.latency;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Const {
  public static final int BUFFER_SIZE = 1024;
  public static final int WARMUP_ITERATIONS = 100;
  public static final int OPERATIONS = 1_000_000;
}
