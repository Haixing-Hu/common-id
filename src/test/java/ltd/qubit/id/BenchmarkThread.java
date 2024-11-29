////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2024.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.id;

public class BenchmarkThread extends Thread {

  private final IdGenerator generator;
  private final long[] values;

  public BenchmarkThread(final IdGenerator generator, final int count) {
    this.generator = generator;
    this.values = new long[count];
  }

  public void run() {
    for (int i = 0; i < values.length; ++i) {
      values[i] = generator.generate();
    }
  }

  public long[] getValues() {
    return values;
  }
}
