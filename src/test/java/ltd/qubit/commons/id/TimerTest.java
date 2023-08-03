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

public class TimerTest {

  private static final int TEST_COUNT = 100;

  @Test
  public void testAtomicIdGeneratorConstructor() {
    final RandomBeanGenerator random = new RandomBeanGenerator();
    for (int i = 0; i < TEST_COUNT; ++i) {
      final Precision precision = random.nextObject(Precision.class);
      final long epochMilli = random.nextLong();
      final Instant epoch = Instant.ofEpochMilli(epochMilli);
      final Timer timer = new Timer(precision, epoch);
      assertEquals(precision, timer.getPrecision());
      assertEquals(epoch, timer.getEpoch());
    }
  }
}
