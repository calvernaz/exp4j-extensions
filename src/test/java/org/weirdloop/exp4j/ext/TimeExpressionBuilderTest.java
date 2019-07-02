package org.weirdloop.exp4j.ext;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import net.objecthunter.exp4j.function.Function;
import org.junit.Test;

public class TimeExpressionBuilderTest {

  Function sma = new Function("sma", 1) {
    @Override
    public double apply(double... args) {
      return args[0];
    }
  };

  @Test
  public void should_validate_time_expressions() {
    TimedExpression expr = new TimeExpressionBuilder("sma(x100680_2_8)")
        .function(sma)
        .variables("x100680_2_8")
        .build();
    assertThat(expr.validate(false).isValid(), is(true));
  }
  
  @Test
  public void should_be_able_to_evaluate_time_expressions_with_normal_operations() {
    TimedExpression expr = new TimeExpressionBuilder("sma(x100680_2_8)/10*20")
        .function(sma)
        .variables("x100680_2_8")
        .build();
  
    expr.consider(3.0);
    System.out.println(expr.evaluate());
  }
}

