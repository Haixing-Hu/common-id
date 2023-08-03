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

/**
 * 此接口定义系统常量。
 *
 * @author 胡海星
 */
public interface Constant {

  /**
   * 主机编号所占据bit数。
   *
   * <p>当前数值为9，因此最多可支持{@code 2^9=512}台不同的主机。
   */
  int HOST_BITS = 9;

  /**
   * 主机编号允许的最小值。
   */
  long HOST_MIN = 0L;

  /**
   * 主机编号允许的最大值。
   */
  long HOST_MAX = (1L << HOST_BITS) - 1L;

  /**
   * 生成模式占据的bit数。
   */
  int MODE_BITS = 1;

  /**
   * 时间戳精度占据的bit数。
   */
  int PRECISION_BITS = 1;

  /**
   * 精度为毫秒时，时间戳占据比特数。
   *
   * <p>当前数值为41，因此最多可支持{@code 2^41/1000/3600/24/365=69}年时间。
   */
  int TIMESTAMP_BITS_IN_MILLISECOND = 41;

  /**
   * 精度为毫秒时，序列号占据比特数。
   *
   * <p>当前数值为12，因此同一个毫秒内最多可以生成{@code 2^12=4096}个不同的序列号。
   */
  int SEQUENCE_BITS_IN_MILLISECOND = 12;

  /**
   * 精度为毫秒时，等待进入下一个时间片的休眠时间，单位为毫秒。
   */
  long WAIT_DURATION_IN_MILLISECOND = 1L;

  /**
   * 精度为秒时，时间戳占据比特数。
   *
   * <p>当前数值为31，因此最多可支持{@code 2^31/3600/24/365=68}年时间。
   */
  int TIMESTAMP_BITS_IN_SECOND = 31;

  /**
   * 精度为秒时，序列号占据比特数.
   *
   * <p>当前数值为22，因此同一个秒内最多可以生成{@code 2^22=4194304}个不同的序列号。
   */
  int SEQUENCE_BITS_IN_SECOND = 22;

  /**
   * 精度为秒时，等待进入下一个时间片的休眠时间，单位为毫秒。
   */
  long WAIT_DURATION_IN_SECOND = 500L;

  /**
   * 默认生成模式为{@link Mode#SEQUENTIAL}。
   */
  Mode DEFAULT_MODE = Mode.SEQUENTIAL;

  /**
   * 默认时间戳精度为{@link Precision#SECOND}。
   */
  Precision DEFAULT_PRECISION = Precision.SECOND;

  /**
   * 默认的EPOCH。
   */
  Instant DEFAULT_EPOCH = Instant.parse("2018-12-02T00:00:00Z");

}
