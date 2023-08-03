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

import javax.annotation.concurrent.Immutable;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import static ltd.qubit.commons.lang.Argument.requireNonNull;

/**
 * 时间戳生成器。
 *
 * @author 胡海星
 */
@Immutable
public class Timer implements Constant {

  static final int MILLIS_PER_SECOND = 1000;

  /**
   * 时间戳精度。
   */
  private final Precision precision;

  /**
   * 时间戳起点。
   */
  private final Instant epoch;

  /**
   * 时间戳计算过程中的被减数。
   */
  private final transient long minuend;

  /**
   * 时间戳计算过程中的除数。
   */
  private final transient long divisor;

  /**
   * 当同一个时间片内的序列号超出允许范围后，需要休眠一段时间等待下一个时间片。
   *
   * <p>此字段记录预先计算好的休眠时间，单位为毫秒。
   */
  private final transient long waitDuration;

  /**
   * 构造一个{@link Timer}对象。
   *
   * @param precision
   *     时间戳精度。
   * @param epoch
   *     时间戳计算起点。
   */
  public Timer(final Precision precision, final Instant epoch) {
    this.precision = requireNonNull("precision", precision);
    this.epoch = requireNonNull("epoch", epoch);
    this.minuend = epoch.toEpochMilli();
    this.divisor = (precision == Precision.SECOND ? MILLIS_PER_SECOND : 1L);
    this.waitDuration = precision.getWaitDuration();
  }

  public final Precision getPrecision() {
    return precision;
  }

  public final Instant getEpoch() {
    return epoch;
  }

  /**
   * 获取当前时间戳。
   *
   * @return 当前的时间戳数值，按照预设的精度从预设的起点开始计算。
   */
  public final long now() {
    return (System.currentTimeMillis() - minuend) / divisor;
  }

  /**
   * 休眠当前线程，直到进入下一个时间片。
   *
   * @param lastTimestamp
   *     上一个时间片的时间戳。
   * @return 等待结束后新时间片的时间戳
   */
  public long waitForNext(final long lastTimestamp) {
    long timestamp = now();
    while (timestamp == lastTimestamp) {
      try {
        Thread.sleep(waitDuration);
      } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      timestamp = now();
    }
    return timestamp;
  }

  /**
   * 获取指定的时间戳对应的真实时刻。
   *
   * @param timestamp
   *     指定的时间戳。
   * @return 该时间戳对应的真实时刻。
   */
  public Instant getInstant(final long timestamp) {
    final long milli = timestamp * divisor + minuend;
    return Instant.ofEpochMilli(milli);
  }

  /**
   * 获取指定的时刻对应的时间戳。
   *
   * @param instant
   *     指定的时刻。
   * @return 该时刻对应的时间戳。
   */
  public long getTimestamp(final Instant instant) {
    final long milli = instant.toEpochMilli();
    return (milli - minuend) / divisor;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Timer other = (Timer) o;
    return Equality.equals(minuend, other.minuend)
        && Equality.equals(divisor, other.divisor)
        && Equality.equals(waitDuration, other.waitDuration)
        && Equality.equals(precision, other.precision)
        && Equality.equals(epoch, other.epoch);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, precision);
    result = Hash.combine(result, multiplier, epoch);
    result = Hash.combine(result, multiplier, minuend);
    result = Hash.combine(result, multiplier, divisor);
    result = Hash.combine(result, multiplier, waitDuration);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("precision", precision)
        .append("epoch", epoch)
        .toString();
  }
}
