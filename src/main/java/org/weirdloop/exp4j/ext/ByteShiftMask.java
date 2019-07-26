package org.weirdloop.exp4j.ext;

import net.objecthunter.exp4j.function.Function;

public class ByteShiftMask extends Function {

	public ByteShiftMask(String name, int nargs) {
		super(name, nargs);
	}

	public static ByteShiftMask create(String name) {
		return new ByteShiftMask(name, 2);
	}

	@Override
	public double apply(double... doubles) {
		int bitMask = 0xFF;
		int value = (int)doubles[0];
		int shift = (int)doubles[1];

		int res = (value >>> shift) & bitMask;
		return res;
	}
}
