package org.scijava.ops.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.scijava.ops.api.OpBuilder;
import org.scijava.ops.api.features.OpMatchingException;
import org.scijava.ops.engine.AbstractTestEnvironment;
import org.scijava.ops.spi.Op;
import org.scijava.ops.spi.OpClass;
import org.scijava.ops.spi.OpCollection;
import org.scijava.ops.spi.OpField;
import org.scijava.types.Nil;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OpsAsParametersTest extends AbstractTestEnvironment implements OpCollection {

	@BeforeAll
	public static void addNeededOps() {
		ops.register(new OpsAsParametersTest());
		ops.register(new FuncClass());
	}

	@OpField(names = "test.parameter.computer")
	public final Function<Number, Double> func = (x) -> x.doubleValue();

	@OpField(names = "test.parameter.op")
	public final BiFunction<List<Number>, Function<Number, Double>, List<Double>> biFunc = (x, op) -> {
		List<Double> output = new ArrayList<>();
		for (Number n : x)
			output.add(op.apply(n));
		return output;
	};

	@Test
	public void TestOpWithNonReifiableFunction() {

		List<Number> list = new ArrayList<>();
		list.add(40l);
		list.add(20.5);
		list.add(4.0d);

		assertThrows(OpMatchingException.class, //
				() -> ops.op("test.parameter.op").input(list, func).outType(new Nil<List<Double>>() {
				}).apply() //
		);
	}

	@Test
	public void TestOpWithOpFieldWithoutRun() {

		List<Number> list = new ArrayList<>();
		list.add(40l);
		list.add(20.5);
		list.add(4.0d);

		BiFunction<List<Number>, Function<Number, Double>, List<Double>> thing = OpBuilder.matchFunction(ops,
				"test.parameter.op", new Nil<List<Number>>() {
				}, new Nil<Function<Number, Double>>() {
				}, new Nil<List<Double>>() {
				});
		

		List<Double> output = thing.apply(list, func);
	}

	@Test
	public void TestOpWithOpClass() {

		List<Number> list = new ArrayList<>();
		list.add(40l);
		list.add(20.5);
		list.add(4.0d);

		Function<Number, Double> funcClass = OpBuilder.matchFunction(ops, "test.parameter.class", new Nil<Number>() {
		}, new Nil<Double>() {
		});

		@SuppressWarnings("unused")
		List<Double> output = ops.op("test.parameter.op").input(list, funcClass).outType(new Nil<List<Double>>() {}).apply();
	}

}

@OpClass(names = "test.parameter.class")
class FuncClass implements Function<Number, Double>, Op {

	@Override
	public Double apply(Number t) {
		return t.doubleValue() + 1;
	}

}
