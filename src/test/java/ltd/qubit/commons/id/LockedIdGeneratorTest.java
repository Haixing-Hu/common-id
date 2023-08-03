////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.id;

import java.time.Instant;

import ltd.qubit.commons.random.RandomBeanGenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of the {@link LockedIdGenerator} class.
 *
 * @author Haixing Hu
 */
public class LockedIdGeneratorTest extends Benchmark implements Constant {

  private static final int TEST_COUNT = 100;

  @Test
  public void testConstructor() {
    final RandomBeanGenerator random = new RandomBeanGenerator();
    for (int i = 0; i < TEST_COUNT; ++i) {
      final Mode mode = random.nextObject(Mode.class);
      final Precision precision = random.nextObject(Precision.class);
      final long host = random.nextLong(HOST_MIN, HOST_MAX);
      final long epochMilli = random.nextLong();
      final Instant epoch = Instant.ofEpochMilli(epochMilli);
      final LockedIdGenerator generator = new LockedIdGenerator(mode, precision, host, epoch);
      assertEquals(mode, generator.getMode());
      assertEquals(precision, generator.getPrecision());
      assertEquals(host, generator.getHost());
      assertEquals(epoch, generator.getEpoch());
    }
  }

  @Test
  public void testGenerateSpecified() {
    final RandomBeanGenerator random = new RandomBeanGenerator();
    for (int i = 0; i < TEST_COUNT; ++i) {
      final Mode mode = random.nextObject(Mode.class);
      final Precision precision = random.nextObject(Precision.class);
      final long host = random.nextLong(HOST_MIN, HOST_MAX);
      final Instant epoch = Instant.now();
      final LockedIdGenerator generator = new LockedIdGenerator(mode, precision, host, epoch);
      final IdBuilder builder = generator.getBuilder();
      final Timer timer = generator.getTimer();
      final long timestamp = random.nextLong(0, builder.getMaxTimestamp() + 1);
      final Instant instant = timer.getInstant(timestamp);
      final long sequence = random.nextLong(0, builder.getMaxSequence() + 1);
      final long id = generator.generate(instant, sequence);
      assertEquals(mode, builder.extractMode(id));
      assertEquals(timestamp, builder.extractTimestamp(id));
      assertEquals(precision, builder.extractPrecision(id));
      assertEquals(host, builder.extractHost(id));
      assertEquals(sequence, builder.extractSequence(id));
    }
  }

  @Test
  public void singleThreadBenchmark_1() {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SEQUENTIAL, Precision.SECOND);
    singleThreadBenchmarkImpl(TOTAL_ID_COUNT, generator);
  }

  @Test
  public void singleThreadBenchmark_2() {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SPREAD,
        Precision.SECOND);
    singleThreadBenchmarkImpl(TOTAL_ID_COUNT, generator);
  }

  @Test
  public void singleThreadBenchmark_3() {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SEQUENTIAL, Precision.MILLISECOND);
    singleThreadBenchmarkImpl(TOTAL_ID_COUNT, generator);
  }

  @Test
  public void singleThreadBenchmark_4() {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SPREAD, Precision.MILLISECOND);
    singleThreadBenchmarkImpl(TOTAL_ID_COUNT, generator);
  }

  @Test
  public void multitheadBenchmark_1() throws Exception {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SEQUENTIAL, Precision.SECOND);
    multiThreadBenchmarkImpl(TOTAL_ID_COUNT, TOTAL_THREAD_COUNT, generator);
  }

  @Test
  public void multitheadBenchmark_2() throws Exception  {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SPREAD, Precision.SECOND);
    multiThreadBenchmarkImpl(TOTAL_ID_COUNT, TOTAL_THREAD_COUNT, generator);
  }

  @Test
  public void multitheadBenchmark_3() throws Exception  {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SEQUENTIAL, Precision.MILLISECOND);
    multiThreadBenchmarkImpl(TOTAL_ID_COUNT, TOTAL_THREAD_COUNT, generator);
  }

  @Test
  public void multitheadBenchmark_4() throws Exception  {
    final LockedIdGenerator generator = new LockedIdGenerator(Mode.SPREAD, Precision.MILLISECOND);
    multiThreadBenchmarkImpl(TOTAL_ID_COUNT, TOTAL_THREAD_COUNT, generator);
  }
}
