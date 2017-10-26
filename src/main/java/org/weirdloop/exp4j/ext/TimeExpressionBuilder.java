package org.weirdloop.exp4j.ext;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;
import net.objecthunter.exp4j.shuntingyard.ShuntingYard;
import net.objecthunter.exp4j.tokenizer.Token;

public class TimeExpressionBuilder {

  private final String expression;
  private final Map<String, Function> userFunctions;

  private final Map<String, Operator> userOperators;

  private final Set<String> variableNames;
  /**
   * Create a new ExpressionBuilder instance and initialize it with a given expression string.
   *
   * @param expression the expression to be parsed
   */
  public TimeExpressionBuilder(String expression) {
    this.expression = expression;
    this.userOperators = new HashMap<>(4);
    this.userFunctions = new HashMap<>(4);
    this.variableNames = new HashSet<>(4);
  }

  /**
   * Add a {@link net.objecthunter.exp4j.function.Function} implementation available for use in the expression
   * @param function the custom {@link net.objecthunter.exp4j.function.Function} implementation that should be available for use in the expression.
   * @return the ExpressionBuilder instance
   */
  public TimeExpressionBuilder function(Function function) {
    this.userFunctions.put(function.getName(), function);
    return this;
  }

  /**
   * Add multiple {@link net.objecthunter.exp4j.function.Function} implementations available for use in the expression
   * @param functions the custom {@link net.objecthunter.exp4j.function.Function} implementations
   * @return the ExpressionBuilder instance
   */
  public TimeExpressionBuilder functions(Function... functions) {
    for (Function f : functions) {
      this.userFunctions.put(f.getName(), f);
    }
    return this;
  }

  /**
   * Add multiple {@link net.objecthunter.exp4j.function.Function} implementations available for use in the expression
   * @param functions the custom {@link net.objecthunter.exp4j.function.Function} implementations
   * @return the ExpressionBuilder instance
   */
  public TimeExpressionBuilder functions(List<Function> functions) {
    for (Function f : functions) {
      this.userFunctions.put(f.getName(), f);
    }
    return this;
  }


  /**
   * Declare variable names used in the expression
   * @param variableNames the variables used in the expression
   * @return the ExpressionBuilder instance
   */
  public TimeExpressionBuilder variables(Set<String> variableNames) {
    this.variableNames.addAll(variableNames);
    return this;
  }

  /**
   * Declare variable names used in the expression
   * @param variableNames the variables used in the expression
   * @return the ExpressionBuilder instance
   */
  public TimeExpressionBuilder variables(String ... variableNames) {
    Collections.addAll(this.variableNames, variableNames);
    return this;
  }

  /**
   * Declare a variable used in the expression
   * @param variableName the variable used in the expression
   * @return the ExpressionBuilder instance
   */
  public TimeExpressionBuilder variable(String variableName) {
    this.variableNames.add(variableName);
    return this;
  }

  public TimedExpression build() {
    Token[] tokens = ShuntingYard.convertToRPN(expression, userFunctions, userOperators,
        variableNames, false);
    return new TimedExpression(tokens, this.userFunctions.keySet());
  }

}
