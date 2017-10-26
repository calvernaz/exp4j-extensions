package org.weirdloop.exp4j.ext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;
import net.objecthunter.exp4j.tokenizer.FunctionToken;
import net.objecthunter.exp4j.tokenizer.NumberToken;
import net.objecthunter.exp4j.tokenizer.OperatorToken;
import net.objecthunter.exp4j.tokenizer.Token;
import net.objecthunter.exp4j.tokenizer.VariableToken;
import org.apache.commons.collections.ArrayStack;
public class TimedExpression {

  private final Token[] tokens;

  private final Map<String, Double> variables;

  private final Set<String> userFunctionNames;

  private static Map<String, Double> createDefaultVariables() {
    final Map<String, Double> vars = new HashMap<>(4);
    vars.put("pi", Math.PI);
    vars.put("π", Math.PI);
    vars.put("φ", 1.61803398874d);
    vars.put("e", Math.E);
    return vars;
  }


  public TimedExpression(TimedExpression existing) {
    this.tokens = Arrays.copyOf(existing.tokens, existing.tokens.length);
    this.variables = new HashMap<>();
    this.variables.putAll(existing.variables);
    this.userFunctionNames = new HashSet<>(existing.userFunctionNames);
  }

  public TimedExpression(final Token[] tokens, Set<String> userFunctionNames) {
    this.tokens = tokens;
    this.variables = createDefaultVariables();
    this.userFunctionNames = userFunctionNames;
  }


  public ValidationResult validate(boolean checkVariablesSet) {
    final List<String> errors = new ArrayList<>(0);
    if (checkVariablesSet) {
            /* check that all vars have a value set */
      for (final Token t : this.tokens) {
        if (t.getType() == Token.TOKEN_VARIABLE) {
          final String var = ((VariableToken) t).getName();
          if (!variables.containsKey(var)) {
            errors.add("The setVariable '" + var + "' has not been set");
          }
        }
      }
    }

    /* Check if the number of operands, functions and operators match.
     The idea is to increment a counter for operands and decrease it for operators.
     When a function occurs the number of available arguments has to be greater
     than or equals to the function's expected number of arguments.
     The count has to be larger than 1 at all times and exactly 1 after all tokens
     have been processed */
    int count = 0;
    for (Token tok : this.tokens) {
      switch (tok.getType()) {
        case Token.TOKEN_NUMBER:
        case Token.TOKEN_VARIABLE:
          count++;
          break;
        case Token.TOKEN_FUNCTION:
          final Function func = ((FunctionToken) tok).getFunction();
          final int argsNum = func.getNumArguments();
          if (argsNum > count) {
            errors.add("Not enough arguments for '" + func.getName() + "'");
          }
          if (argsNum > 1) {
            count -= argsNum - 1;
          } else if (argsNum == 0) {
            // see https://github.com/fasseg/exp4j/issues/59
            count++;
          }
          break;
        case Token.TOKEN_OPERATOR:
          Operator op = ((OperatorToken) tok).getOperator();
          if (op.getNumOperands() == 2) {
            count--;
          }
          break;
      }
      if (count < 1) {
        errors.add("Too many operators");
        return new ValidationResult(false, errors);
      }
    }
    if (count > 1) {
      errors.add("Too many operands");
    }
    return errors.size() == 0 ? ValidationResult.SUCCESS : new ValidationResult(false, errors);

  }

  public ValidationResult validate() {
    return validate(false);
  }

  public double evaluate() {
    final ArrayStack output = new ArrayStack();
    for (int i = 0; i < tokens.length; i++) {
      Token t = tokens[i];
      if (t.getType() == Token.TOKEN_NUMBER) {
        output.push(((NumberToken) t).getValue());
      } else if (t.getType() == Token.TOKEN_OPERATOR) {

      } else if (t.getType() == Token.TOKEN_FUNCTION) {
        FunctionToken func = (FunctionToken) t;
        //func.getFunction().getName()

        double[] args = new double[0];
        output.push(func.getFunction().apply(args));
      }
    }
    if (output.size() > 1) {
      throw new IllegalArgumentException(
          "Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
    }
    return (double) output.pop();
  }

}
