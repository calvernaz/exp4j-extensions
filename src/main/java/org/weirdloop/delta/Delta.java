package org.weirdloop.delta;

import net.objecthunter.exp4j.function.Function;

public class Delta extends Function {

  private Double delta = null;

  public static Delta create(String name, int nparam) {
    return new Delta(name, nparam);
  }

  private Delta(String name, int nparam) {
    super(name, nparam);
  }

  public double apply(double... doubles) {
    double cur = doubles[0];
    if (delta == null) delta = cur;

    double res = Math.abs(cur - delta);
    delta = doubles[0];
    return res;
  }
}
