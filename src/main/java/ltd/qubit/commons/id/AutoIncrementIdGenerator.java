////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 实现自增的ID生成器，模拟数据库的自增ID操作。
 *
 * <p>此ID生成器将生成从1开始顺序递增的ID。</p>
 *
 * @author 胡海星
 */
public class AutoIncrementIdGenerator implements IdGenerator {

  private final AtomicLong nextId = new AtomicLong(0);

  @Override
  public Mode getMode() {
    return Mode.SEQUENTIAL;
  }

  @Override
  public Precision getPrecision() {
    return null;
  }

  @Override
  public long generate() {
    return nextId.incrementAndGet();
  }

  @Override
  public void reset() {
    nextId.set(0);
  }
}
