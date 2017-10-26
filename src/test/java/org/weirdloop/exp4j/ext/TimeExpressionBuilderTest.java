package org.weirdloop.exp4j.ext;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import net.objecthunter.exp4j.function.Function;
import org.junit.Test;

public class TimeExpressionBuilderTest {

  Function sma = new Function("sma", 1) {
    @Override
    public double apply(double... args) {
      return 0.0;
    }
  };

  @Test
  public void test() {
    String expression = "sma(x100680_2_8)";
    TimedExpression expr = new TimeExpressionBuilder(expression)
        .function(sma)
        .variables("x100680_2_8")
        .build();
    assertThat(expr.validate(false).isValid(), is(true));

  }
}
