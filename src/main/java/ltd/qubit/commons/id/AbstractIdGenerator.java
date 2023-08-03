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

import javax.annotation.concurrent.ThreadSafe;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

/**
 * ID生成器的抽象基类。
 *
 * <p>这个类是线程安全的，可以用于多线程环境。
 *
 * @author 胡海星
 */
@ThreadSafe
public abstract class AbstractIdGenerator implements IdGenerator {

  /**
   * 时间戳生成器。
   */
  protected final Timer timer;

  /**
   * ID构造器。
   */
  protected final IdBuilder builder;

  /**
   * 构造一个ID生成器。
   *
   * @param mode
   *     该ID生成器的生成模式，不可为{@code null}。
   * @param precision
   *     该ID生成器的时间戳精度，不可为{@code null}。
   * @param host
   *     该ID生成器的主机编号，必须在 {@code [0, 512)} 之间。
   * @param epoch
   *     时间戳起点。
   */
  public AbstractIdGenerator(final Mode mode, final Precision precision,
      final long host, final Instant epoch) {
    this.timer = new Timer(precision, epoch);
    this.builder = new IdBuilder(mode, precision, host);
  }

  public final Mode getMode() {
    return builder.getMode();
  }

  public final Precision getPrecision() {
    return builder.getPrecision();
  }

  public final long getHost() {
    return builder.getHost();
  }

  public final Instant getEpoch() {
    return timer.getEpoch();
  }

  public final Timer getTimer() {
    return timer;
  }

  public final IdBuilder getBuilder() {
    return builder;
  }

  /**
   * 生成下一个ID。
   *
   * @return
   *     生成的ID。
   */
  public abstract long generate();

  /**
   * 根据指定的时刻和序列号，生成ID。
   *
   * @param instant
   *     指定的时刻。
   * @param sequence
   *     指定的序列号。
   * @return 生成的ID。
   * @throws IllegalArgumentException
   *     若指定的时刻或序列号超过允许范围。
   */
  public long generate(final Instant instant, final long sequence) {
    if (sequence > builder.getMaxSequence()) {
      throw new IllegalArgumentException("The sequence overflows.");
    }
    final long timestamp = timer.getTimestamp(instant);
    if (timestamp > builder.getMaxTimestamp()) {
      throw new IllegalArgumentException("The timestamp overflows.");
    }
    return builder.build(timestamp, sequence);
  }

  @Override
  public void reset() {
    //  do nothing
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final AbstractIdGenerator other = (AbstractIdGenerator) o;
    return Equality.equals(timer, other.timer)
            && Equality.equals(builder, other.builder);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, timer);
    result = Hash.combine(result, multiplier, builder);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("timer", timer)
            .append("builder", builder)
            .toString();
  }
}
