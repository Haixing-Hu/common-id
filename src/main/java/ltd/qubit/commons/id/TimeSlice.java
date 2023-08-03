////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.id;

import javax.annotation.concurrent.Immutable;

/**
 * 此模型表示时间片对象。
 *
 * @author 胡海星
 */
@Immutable
public class TimeSlice {

  /**
   * 该时间片对应的时间戳。
   */
  public final long timestamp;

  /**
   * 该时间片中当前序列号。
   */
  public final long sequence;

  public TimeSlice(final long timestamp) {
    this.timestamp = timestamp;
    this.sequence = 0;
  }

  public TimeSlice(final long timestamp, final long sequence) {
    this.timestamp = timestamp;
    this.sequence = sequence;
  }
}
