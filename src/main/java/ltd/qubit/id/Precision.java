////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2024.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.id;

import static ltd.qubit.id.Constant.SEQUENCE_BITS_IN_MILLISECOND;
import static ltd.qubit.id.Constant.SEQUENCE_BITS_IN_SECOND;
import static ltd.qubit.id.Constant.TIMESTAMP_BITS_IN_MILLISECOND;
import static ltd.qubit.id.Constant.TIMESTAMP_BITS_IN_SECOND;
import static ltd.qubit.id.Constant.WAIT_DURATION_IN_MILLISECOND;
import static ltd.qubit.id.Constant.WAIT_DURATION_IN_SECOND;

/**
 * 此枚举表示时间戳精度。
 *
 * @author 胡海星
 */
public enum Precision {

  /**
   * 时间戳精度为毫秒。
   *
   * <p>采用此精度，同一毫秒内序列号不重复，时间戳粒度较细，但每毫秒可承受的峰值有限，最多只能生
   * 成4096个ID。同一毫秒内如果有更多的请求，只能等到下一毫秒再响应。
   */
  MILLISECOND(TIMESTAMP_BITS_IN_MILLISECOND, SEQUENCE_BITS_IN_MILLISECOND,
      WAIT_DURATION_IN_MILLISECOND),

  /**
   * 时间戳精度为秒。
   *
   * <p>采用此精度，同一秒内序列号不重复，时间戳粒度较大，每秒可承受的峰值较高，最多可生成400多
   * 万个ID。同一秒内如果有更多的请求，只能等到下一秒再响应。
   */
  SECOND(TIMESTAMP_BITS_IN_SECOND, SEQUENCE_BITS_IN_SECOND,
      WAIT_DURATION_IN_SECOND);

  /**
   * 此精度下时间戳所占据的bit数目。
   */
  private final int timestampBits;

  /**
   * 此精度下序列号所占据的bit数目。
   */
  private final int sequenceBits;

  /**
   * 此精度下到达下一个时间片所需等待的时间，单位为毫秒。
   */
  private final long waitDuration;

  Precision(final int timestampBits, final int sequenceBits, final long waitDuration) {
    this.timestampBits = timestampBits;
    this.sequenceBits = sequenceBits;
    this.waitDuration = waitDuration;
  }

  public final int getTimestampBits() {
    return timestampBits;
  }

  public final int getSequenceBits() {
    return sequenceBits;
  }

  public final long getWaitDuration() {
    return waitDuration;
  }
}
