////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2024.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.id;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.ThreadSafe;

/**
 * 通过互斥锁实现的ID生成器。
 *
 * <p>这个类是线程安全的，可以用于多线程环境。
 *
 * @author 胡海星
 */
@ThreadSafe
public class LockedIdGenerator extends AbstractIdGenerator {

  /**
   * 上一个时间戳。
   */
  public volatile long lastTimestamp;

  /**
   * 当前序列号。
   */
  public volatile long sequence;

  /**
   * 互斥锁。
   */
  private final Lock lock;

  /**
   * 构造默认的ID生成器。
   */
  public LockedIdGenerator() {
    this(DEFAULT_MODE, DEFAULT_PRECISION, 0L, Instant.now());
  }

  /**
   * 构造一个ID生成器。
   *
   * @param mode
   *     该ID生成器的生成模式，不可为{@code null}。
   * @param precision
   *     该ID生成器的时间戳精度，不可为{@code null}。
   */
  public LockedIdGenerator(final Mode mode, final Precision precision) {
    this(mode, precision, 0L, Instant.now());
  }

  /**
   * 构造一个ID生成器。
   *
   * @param host
   *     该ID生成器的主机编号，必须在 {@code [0, 512)} 之间。
   * @param epoch
   *     时间戳起点。
   */
  public LockedIdGenerator(final long host, final Instant epoch) {
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
   * @param epoch
   *     时间戳起点。
   */
  public LockedIdGenerator(final Mode mode, final Precision precision,
      final long host, final Instant epoch) {
    super(mode, precision, host, epoch);
    this.lock = new ReentrantLock();
  }

  /**
   * 生成下一个ID。
   *
   * @return 生成的ID。
   */
  public final long generate() {
    final long maxSequence = builder.getMaxSequence();
    long timestamp;
    long seq;
    lock.lock();
    try {
      timestamp = timer.now();
      if (timestamp == lastTimestamp) { // 本次调用和上次调用在同一个时间片内，增加序号计数器
        seq = ++sequence;
        if (sequence > maxSequence) {
          // 序号超出上界，等待进入下一个时间片
          timestamp = timer.waitForNext(timestamp);
          seq = sequence = 0;
        }
      } else {  // 本次调用和上次调用不在同一个时间片内，重置序号计数器
        seq = sequence = 0;
      }
      lastTimestamp = timestamp;
    } finally {
      lock.unlock();
    }
    return builder.build(timestamp, seq);
  }
}
