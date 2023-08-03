////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.id;

import ltd.qubit.commons.random.RandomBeanGenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link IdBuilder}的单元测试。
 *
 * @author 胡海星
 */
public class IdBuilderTest implements Constant {

  //  stop magic number check
  @Test
  public void testBuild_1() {
    final IdBuilder b1 = new IdBuilder(Mode.SEQUENTIAL, Precision.SECOND, 317L);
    // mode = 0
    // precision = 1
    // host = 100111101
    // timestamp = 0000000000100101101011010000111
    // sequence = 1010110100011111000111
    // ID = 0 0000000000100101101011010000111 1 100111101 1010110100011111000111
    long id = 0B0000000000010010110101101000011111001111011010110100011111000111L;
    assertEquals(id, b1.build(1234567L, 2836423L));
    assertEquals(Mode.SEQUENTIAL, b1.extractMode(id));
    assertEquals(1234567L, b1.extractTimestamp(id));
    assertEquals(Precision.SECOND, b1.extractPrecision(id));
    assertEquals(317L, b1.extractHost(id));
    assertEquals(2836423L, b1.extractSequence(id));

    // change the host to 000111101
    final IdBuilder b2 = new IdBuilder(Mode.SEQUENTIAL, Precision.SECOND, 0B000111101L);
    // mode = 0
    // precision = 1
    // host = 000111101
    // timestamp = 0000000000100101101011010000111
    // sequence = 1010110100011111000111
    // ID = 0 0000000000100101101011010000111 1 000111101 1010110100011111000111
    id = 0B0000000000010010110101101000011110001111011010110100011111000111L;
    assertEquals(id, b2.build(1234567L, 2836423L));
    assertEquals(Mode.SEQUENTIAL, b2.extractMode(id));
    assertEquals(1234567L, b2.extractTimestamp(id));
    assertEquals(Precision.SECOND, b2.extractPrecision(id));
    assertEquals(0B000111101L, b2.extractHost(id));
    assertEquals(2836423L, b2.extractSequence(id));
  }

  @Test
  public void testBuild_2() {
    final IdBuilder b1 = new IdBuilder(Mode.SPREAD, Precision.SECOND, 317L);
    // mode = 1
    // precision = 1
    // host = 100111101
    // timestamp = 0000000000100101101011010000111 -> 1110000101101011010010000000000
    // sequence = 1010110100011111000111
    // ID = 1 1110000101101011010010000000000 1 100111101 1010110100011111000111
    long id = 0B1111000010110101101001000000000011001111011010110100011111000111L;
    assertEquals(id, b1.build(1234567L, 2836423L));
    assertEquals(Mode.SPREAD, b1.extractMode(id));
    assertEquals(1234567L, b1.extractTimestamp(id));
    assertEquals(Precision.SECOND, b1.extractPrecision(id));
    assertEquals(317L, b1.extractHost(id));
    assertEquals(2836423L, b1.extractSequence(id));

    // change the host to 111110101
    final IdBuilder b2 = new IdBuilder(Mode.SPREAD, Precision.SECOND, 0B111110101L);
    // mode = 1
    // precision = 1
    // host = 111110101
    // timestamp = 0000000000100101101011010000111 -> 1110000101101011010010000000000
    // sequence = 1010110100011111000111
    // ID = 1 1110000101101011010010000000000 1 111110101 1010110100011111000111
    id = 0B1111000010110101101001000000000011111101011010110100011111000111L;
    assertEquals(id, b2.build(1234567L, 2836423L));
    assertEquals(Mode.SPREAD, b2.extractMode(id));
    assertEquals(1234567L, b2.extractTimestamp(id));
    assertEquals(Precision.SECOND, b2.extractPrecision(id));
    assertEquals(0B111110101L, b2.extractHost(id));
    assertEquals(2836423L, b2.extractSequence(id));
  }

  @Test
  public void testBuild_3() {
    final IdBuilder b1 = new IdBuilder(Mode.SEQUENTIAL, Precision.MILLISECOND, 317L);
    // mode = 0
    // precision = 0
    // host = 100111101
    // timestamp = 00000000000000000000100101101011010000111
    // sequence = 100001000101
    // ID = 0 00000000000000000000100101101011010000111 0 100111101 100001000101
    long id = 0B0000000000000000000001001011010110100001110100111101100001000101L;
    assertEquals(id, b1.build(1234567L, 2117L));
    assertEquals(Mode.SEQUENTIAL, b1.extractMode(id));
    assertEquals(1234567L, b1.extractTimestamp(id));
    assertEquals(Precision.MILLISECOND, b1.extractPrecision(id));
    assertEquals(317L, b1.extractHost(id));
    assertEquals(2117L, b1.extractSequence(id));

    // change the host to 100011101
    final IdBuilder b2 = new IdBuilder(Mode.SEQUENTIAL, Precision.MILLISECOND, 0B100011101L);
    // mode = 0
    // precision = 0
    // host = 100011101
    // timestamp = 00000000000000000000100101101011010000111
    // sequence = 100001000101
    // ID = 0 00000000000000000000100101101011010000111 0 100011101 100001000101
    id = 0B0000000000000000000001001011010110100001110100011101100001000101L;
    assertEquals(id, b2.build(1234567L, 2117L));
    assertEquals(Mode.SEQUENTIAL, b2.extractMode(id));
    assertEquals(1234567L, b2.extractTimestamp(id));
    assertEquals(Precision.MILLISECOND, b2.extractPrecision(id));
    assertEquals(0B100011101L, b2.extractHost(id));
    assertEquals(2117L, b2.extractSequence(id));
  }

  @Test
  public void testBuild_4() {
    final IdBuilder b1 = new IdBuilder(Mode.SPREAD, Precision.MILLISECOND, 317L);
    // mode = 1
    // precision = 0
    // host = 100111101
    // timestamp = 00000000000000000000100101101011010000111
    // -> 11100001011010110100100000000000000000000
    // sequence = 100001000101
    // ID = 1 11100001011010110100100000000000000000000 0 100111101 100001000101
    long id = 0B1111000010110101101001000000000000000000000100111101100001000101L;
    assertEquals(id, b1.build(1234567L, 2117L));
    assertEquals(Mode.SPREAD, b1.extractMode(id));
    assertEquals(1234567L, b1.extractTimestamp(id));
    assertEquals(Precision.MILLISECOND, b1.extractPrecision(id));
    assertEquals(317L, b1.extractHost(id));
    assertEquals(2117L, b1.extractSequence(id));

    // change the host to 100010011
    final IdBuilder b2 = new IdBuilder(Mode.SPREAD, Precision.MILLISECOND, 0B100010011L);
    // mode = 1
    // precision = 0
    // host = 100010011
    // timestamp = 00000000000000000000100101101011010000111
    //  -> 11100001011010110100100000000000000000000
    // sequence = 100001000101
    // ID = 1 11100001011010110100100000000000000000000 0 100010011 100001000101
    id = 0B1111000010110101101001000000000000000000000100010011100001000101L;
    assertEquals(id, b2.build(1234567L, 2117L));
    assertEquals(Mode.SPREAD, b2.extractMode(id));
    assertEquals(1234567L, b2.extractTimestamp(id));
    assertEquals(Precision.MILLISECOND, b2.extractPrecision(id));
    assertEquals(0B100010011L, b2.extractHost(id));
    assertEquals(2117L, b2.extractSequence(id));
  }

  //  resume magic number check

  private static final int TEST_COUNT = 100;

  @Test
  public void testBuilderConstructor() {
    final RandomBeanGenerator random = new RandomBeanGenerator();
    for (int i = 0; i < TEST_COUNT; ++i) {
      final Mode mode = random.nextObject(Mode.class);
      final Precision precision = random.nextObject(Precision.class);
      final long host = random.nextLong(HOST_MIN, HOST_MAX);
      final IdBuilder builder = new IdBuilder(mode, precision, host);
      assertEquals(mode, builder.getMode());
      assertEquals(precision, builder.getPrecision());
      assertEquals(host, builder.getHost());
      assertEquals((1L << precision.getTimestampBits()) - 1L, builder.getMaxTimestamp());
      assertEquals((1L << precision.getSequenceBits()) - 1L, builder.getMaxSequence());
    }
  }
}
