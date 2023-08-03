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
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.concurrent.ThreadSafe;

/**
 * 通过原子操作实现的ID生成器。
 *
 * <p>这个类是线程安全的，可以用于多线程环境。
 *
 * @author 胡海星
 */
@ThreadSafe
public class AtomicIdGenerator extends AbstractIdGenerator {

  /**
   * 时间片。
   */
  private final AtomicReference<TimeSlice> slice;

  /**
   * 构造默认的ID生成器。
   */
  public AtomicIdGenerator() {
    this(DEFAULT_MODE, DEFAULT_PRECISION, 0L, DEFAULT_EPOCH);
  }

  /**
   * 构造一个ID生成器。
   *
   * @param mode
   *     该ID生成器的生成模式，不可为{@code null}。
   * @param precision
   *     该ID生成器的时间戳精度，不可为{@code null}。
   */
  public AtomicIdGenerator(final Mode mode, final Precision precision) {
    this(mode, precision, 0L, DEFAULT_EPOCH);
  }

  /**
   * 构造一个ID生成器。
   *
   * @param host
   *     该ID生成器的主机编号，必须在 {@code [0, 512)} 之间。
   * @param epoch
   *     时间戳起点。
   */
  public AtomicIdGenerator(final long host, final Instant epoch) {
    this(DEFAULT_MODE, DEFAULT_PRECISION, host, epoch);
  }

  /**
   * 构造一个ID生成器。
   *
   * @param mode
   *     该ID生成器的生成模式，不可为{@code null}。
   * @param precision
   *     该ID生成器的时间戳精度，不可为{@code null}。
   * @param host
   *     该ID生成器的主机编号，必须在 {@code [0, 512)} 之间。
   */
  public AtomicIdGenerator(final Mode mode, final Precision precision, final long host) {
    this(mode, precision, host, DEFAULT_EPOCH);
  }

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
  public AtomicIdGenerator(final Mode mode, final Precision precision, final long host,
      final Instant epoch) {
    super(mode, precision, host, epoch);
    this.slice = new AtomicReference<>(new TimeSlice(0L));
  }

  /**
   * 生成下一个ID。
   *
   * @return 生成的ID。
   */
  public final long generate() {
    final long maxSequence = builder.getMaxSequence();
    while (true) {
      final TimeSlice oldSlice = slice.get();
      long timestamp = timer.now();
      final long sequence;
      if (timestamp == oldSlice.timestamp) {
        // 本次调用和上次调用在同一个时间片内，增加序号计数器
        sequence = (oldSlice.sequence + 1) & maxSequence;
        if (sequence == 0) {
          // 序号超出上界，等待进入下一个时间片
          timestamp = timer.waitForNext(timestamp);
        }
      } else {
        // 本次调用和上次调用不在同一个时间片内，重置序号计数器
        sequence = 0;
      }
      final TimeSlice newSlice = new TimeSlice(timestamp, sequence);
      if (slice.compareAndSet(oldSlice, newSlice)) {
        // 运行到这里表明前面的操作没有被其他线程打断
        return builder.build(timestamp, sequence);
      }
      // 否则，重头尝试
    }
  }
}
