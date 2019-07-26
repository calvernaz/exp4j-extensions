package org.weirdloop.exp4j.ext;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ByteShiftMaskTest {

	private ByteShiftMask bsm;

	@Before
	public void setup() {
		bsm = ByteShiftMask.create("bsm");
	}

	@Test
	public void high_byte_shift() {
		double result = expr()
				.setVariable("x100680_0_2", 5670)
				.evaluate();
		assertThat(result, is(38.0));
	}

	private Expression expr() {
		return new ExpressionBuilder("bsm(x100680_0_2, 0)")
				.variable("x100680_0_2")
				.function(bsm)
				.build();
	}
}