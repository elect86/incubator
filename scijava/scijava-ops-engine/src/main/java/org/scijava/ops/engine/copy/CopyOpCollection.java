package org.scijava.ops.engine.copy;

import org.scijava.priority.Priority;
import org.scijava.function.Computers;
import org.scijava.ops.spi.OpCollection;
import org.scijava.ops.spi.OpField;

public class CopyOpCollection <T> implements OpCollection{

	@OpField(names = "cp, copy", priority = Priority.LOW, params = "array, arrayCopy")
	public static final Computers.Arity1<double[], double[]> copyPrimitiveDoubleArray = (from, to) -> {
		for (int i = 0; i < to.length; i++) {
			to[i] = from[i];
		}
	};
	
	@OpField(names = "cp, copy", priority = Priority.LOW, params = "array, arrayCopy")
	public static final Computers.Arity1<Double[], Double[]> copyDoubleArray = (from, to) -> {
		for (int i = 0; i < to.length; i++) {
			to[i] = from[i];
		}
	};

	@OpField(names = "cp, copy", params = "array, arrayCopy")
	public final Computers.Arity1<T[], T[]> copyGenericArray = (from, to) -> {
		int arrMax = Math.max(from.length, to.length);
		System.arraycopy(from, 0, to, 0, arrMax);
	};
}
