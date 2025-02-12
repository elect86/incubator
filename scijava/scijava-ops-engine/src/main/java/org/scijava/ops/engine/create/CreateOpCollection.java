package org.scijava.ops.engine.create;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.scijava.priority.Priority;
import org.scijava.function.Producer;
import org.scijava.ops.spi.OpCollection;
import org.scijava.ops.spi.OpField;

/**
 * Creation ops
 * @author Gabriel Selzer
 *
 */
public class CreateOpCollection implements OpCollection{

	@OpField(names = "create, src, source", priority = Priority.LOW, params = "array, arrayLike")
	public static final Function<double[], double[]> createdoubleArrayInputAware = from -> new double[from.length];

	@OpField(names = "create, src, source", priority = Priority.LOW, params = "array, arrayLike")
	public static final Function<Double[], Double[]> createDoubleArrayInputAware = from -> new Double[from.length];

	@OpField(names = "create, src, source", priority = Priority.LOW, params = "array, arrayLike")
	public static final Function<int[], int[]> createintArrayInputAware = from -> new int[from.length];

	@OpField(names = "create, src, source", priority = Priority.LOW, params = "array, arrayLike")
	public static final Function<Integer[], Integer[]> createIntegerArrayInputAware = from -> new Integer[from.length];
	
	@OpField(names = "create, src, source", priority = Priority.LOW, params = "array1, array2, arrayLike")
	public static final BiFunction<double[], double[], double[]> createDoubleArrayBiInputAware = (i1, i2) -> {
		if (i1.length != i2.length) {
			throw new IllegalArgumentException("Input array length muss be equal");
		}
		return new double[i1.length];
	};
	
	@OpField(names = "create", priority = Priority.HIGH)
	public static final Producer<Double> doubleSource = () -> 0.0;
}
