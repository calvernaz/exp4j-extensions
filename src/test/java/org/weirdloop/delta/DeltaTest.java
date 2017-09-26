package org.weirdloop.delta;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.junit.Before;
import org.junit.Test;

public class DeltaTest {

  private Delta deltaDiff;

  @Before
  public void setup() {
    deltaDiff = Delta.create("delta", 1);
  }

  @Test
  public void delta_with_no_history() {
    double result = expr()
        .setVariable("x100680_0_2", 3.0)
        .evaluate();
    assertThat(result, is(3.0));
  }

  @Test
  public void delta_between_two_values() {
    double r1 = expr()
        .setVariable("x100680_0_2", 3.0)
        .evaluate();

    assertThat(r1, is(3.0));

    double r2 = expr()
        .setVariable("x100680_0_2", 1.0)
        .evaluate();

    assertThat(r2, is(2.0));
  }

  @Test
  public void delta_neg_into_pos() {
    double r1 = expr()
        .setVariable("x100680_0_2", 5.0)
        .evaluate();

    assertThat(r1, is(5.0));

    double r2 = expr()
        .setVariable("x100680_0_2", 6.0)
        .evaluate();

    assertThat(r2, is(1.0));
  }

  public Expression expr() {
    return new ExpressionBuilder("delta(x100680_0_2)")
        .variable("x100680_0_2")
        .function(deltaDiff)
        .build();
  }
}
