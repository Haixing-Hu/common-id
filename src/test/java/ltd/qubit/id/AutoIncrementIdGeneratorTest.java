////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2024.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of the {@link AutoIncrementIdGenerator} class.
 *
 * @author Haixing Hu
 */
public class AutoIncrementIdGeneratorTest extends Benchmark implements Constant {

  private static final int MAX = 10000;

  @Test
  public void testGenerate() {
    final IdGenerator generator = new AutoIncrementIdGenerator();
    for (int i = 0; i < MAX; ++i) {
      assertEquals(i + 1, generator.generate());
    }
  }


  @Test
  public void singleThreadBenchmark() {
    final IdGenerator generator = new AutoIncrementIdGenerator();
    singleThreadBenchmarkImpl(TOTAL_ID_COUNT, generator);
  }

  @Test
  public void multitheadBenchmark() throws Exception {
    final IdGenerator generator = new AutoIncrementIdGenerator();
    multiThreadBenchmarkImpl(TOTAL_ID_COUNT, TOTAL_THREAD_COUNT, generator);
  }

  @Test
  public void testReset() {
    final IdGenerator generator = new AutoIncrementIdGenerator();
    for (int i = 0; i < MAX; ++i) {
      assertEquals(i + 1, generator.generate());
    }
    generator.reset();
    for (int i = 0; i < MAX; ++i) {
      assertEquals(i + 1, generator.generate());
    }
  }
}
