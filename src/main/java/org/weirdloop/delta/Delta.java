package org.weirdloop.delta;

import net.objecthunter.exp4j.function.Function;

public class Delta extends Function {

  private final Double ZERO = 0.0;
  private Double delta;

  public static Delta create(String name, int nparam) {
    return new Delta(name, nparam);
  }

  private Delta(String name, int nparam) {
    super(name, nparam);
    delta = ZERO;
  }

  public double apply(double... doubles) {
    delta = Math.abs(doubles[0] - delta);
    return delta;
  }
}
