////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2024.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.id;

/**
 * ID生成器接口。
 *
 * @author 胡海星
 */
public interface IdGenerator extends Constant {

  /**
   * 获取ID生成模式。
   *
   * @return
   *     ID生成模式。
   */
  Mode getMode();

  /**
   * 获取时间戳精度。
   *
   * @return
   *     时间戳精度。
   */
  Precision getPrecision();

  /**
   * 生成下一个ID。
   *
   * @return
   *     生成的ID。
   */
  long generate();

  /**
   * 重置此ID生成器。
   */
  void reset();
}
