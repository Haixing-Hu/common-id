////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.id;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import ltd.qubit.commons.math.LongBit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.currentTimeMillis;

import static ltd.qubit.commons.lang.ClassUtils.getShortClassName;
import static ltd.qubit.commons.util.HumanReadable.formatDuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Benchmark {

  protected static final int TOTAL_THREAD_COUNT = 100;
  protected static final int TOTAL_ID_COUNT = 5000000;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private void assertNoDuplicated(final IdGenerator generator,
      final long[] values) {
    logger.info("Checking generated IDs ....");
    final HashSet<Long> set = new HashSet<>();
    long last = 0;
    final long start = currentTimeMillis();
    for (final long id : values) {
      assertFalse(set.contains(id),
          "The ID " + id + " should not have been generated.");
      set.add(id);
      if (generator.getMode() == Mode.SEQUENTIAL) {
        assertTrue(LongBit.compare(last, id) < 0, "The sequential generator "
            + "should generate sequential IDs: last = " + last
            + ", id = " + id);
        last = id;
      }
    }
    final long end = currentTimeMillis();
    final String time = formatDuration(end - start, TimeUnit.MILLISECONDS);
    logger.info("Finished in {}. No duplicated found.", time);
  }

  private void assertNoDuplicated(final IdGenerator generator,
      final BenchmarkThread[] threads) {
    logger.info("Checking generated IDs ....");
    final HashSet<Long> set = new HashSet<>();
    final long start = currentTimeMillis();
    for (final BenchmarkThread thread : threads) {
      final long[] values = thread.getValues();
      long last = 0;
      for (final long id : values) {
        assertFalse(set.contains(id));
        set.add(id);
        if (generator.getMode() == Mode.SEQUENTIAL) {
          assertTrue(LongBit.compare(last, id) < 0, "The sequential generator "
              + "should generate sequential IDs: last = " + last
              + ", id = " + id);
          last = id;
        }
      }
    }
    final long end = currentTimeMillis();
    final String time = formatDuration(end - start, TimeUnit.MILLISECONDS);
    logger.info("Finished in {}. No duplicated found.", time);
  }

  protected void singleThreadBenchmarkImpl(final int count,
      final IdGenerator generator) {
    final long[] values = new long[count];
    final String generatorName = getShortClassName(generator.getClass());
    logger.info("Generating {} IDs in a single thread with {}({}, {}) ...",
        count, generatorName, generator.getMode(), generator.getPrecision());
    final long start = currentTimeMillis();
    for (int i = 0; i < count; ++i) {
      values[i] = generator.generate();
    }
    final long end = currentTimeMillis();
    final String time = formatDuration(end - start, TimeUnit.MILLISECONDS);
    logger.info("Finished in {}. Average speed is {}/s.", time,
        ((long) count * Timer.MILLIS_PER_SECOND / (end - start)));
    assertNoDuplicated(generator, values);
  }

  protected void multiThreadBenchmarkImpl(final int totalIdCount,
      final int threadCount, final IdGenerator generator) throws Exception {
    final int count = totalIdCount / threadCount;
    final BenchmarkThread[] threads = new BenchmarkThread[threadCount];
    for (int i = 0; i < threadCount; ++i) {
      threads[i] = new BenchmarkThread(generator, count);
    }
    final String generatorName = getShortClassName(generator.getClass());
    logger.info("Generating {} IDs in {} threads with {}({}, {})...",
        totalIdCount, threadCount, generatorName, generator.getMode(),
        generator.getPrecision());
    final long start = currentTimeMillis();
    for (int i = 0; i < threadCount; ++i) {
      threads[i].start();
    }
    for (int i = 0; i < threadCount; ++i) {
      threads[i].join();
    }
    final long end = currentTimeMillis();
    final String time = formatDuration(end - start, TimeUnit.MILLISECONDS);
    logger.info("Finished in {}. Average speed is {}/s.", time,
        ((long) totalIdCount * Timer.MILLIS_PER_SECOND / (end - start)));
    assertNoDuplicated(generator, threads);
  }
}
