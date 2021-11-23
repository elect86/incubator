/*
 * #%L
 * SciJava Operations: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2016 - 2019 SciJava Ops developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

/*
* This is autogenerated source code -- DO NOT EDIT. Instead, edit the
* corresponding template in templates/ and rerun bin/generate.groovy.
*/

package org.scijava.ops.engine.adapt.complexLift;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scijava.function.Computers;
import org.scijava.ops.engine.AbstractTestEnvironment;
import org.scijava.ops.engine.OpBuilderTestOps;
import org.scijava.ops.engine.adapt.functional.ComputersToFunctionsViaFunction;
import org.scijava.ops.engine.adapt.lift.FunctionToIterables;
import org.scijava.ops.engine.create.CreateOpCollection;
import org.scijava.types.Nil;

/**
 * Tests the adaptation of {@link Computers} running on a type into
 * {@link Computers} running on an {@link Iterable} of that type.
 * 
 * @author Gabriel Selzer
 *
 */
public class ComputerToFunctionIterablesTest extends AbstractTestEnvironment {

	@BeforeClass
	public static void AddNeededOps() {
		Object[] lifters = objsFromNoArgConstructors(ComputersToFunctionsAndLift.class.getDeclaredClasses());
		discoverer.register(lifters);
		discoverer.register(new FunctionToIterables());
		Object[] adapters = objsFromNoArgConstructors(ComputersToFunctionsViaFunction.class.getDeclaredClasses());
		discoverer.register(adapters);
		discoverer.register(new CreateOpCollection());
		discoverer.register(new OpBuilderTestOps());
	}

	@Test
	public void testComputer1ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {1., 2., 3. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer2ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {2., 4., 6. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer3ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {3., 6., 9. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer4ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {4., 8., 12. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer5ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {5., 10., 15. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer6ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {6., 12., 18. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer7ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {7., 14., 21. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer8ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {8., 16., 24. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer9ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {9., 18., 27. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer10ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {10., 20., 30. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer11ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {11., 22., 33. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer12ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {12., 24., 36. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer13ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {13., 26., 39. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer14ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {14., 28., 42. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer15ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {15., 30., 45. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

	@Test
	public void testComputer16ToIterables() {
		final List<double[]> in = Arrays.asList(new double[] { 1, 2, 3 });
		final List<double[]> expected = Arrays.asList(new double[] {16., 32., 48. });
		final Iterable<double[]> out = ops.op("test.addArrays").input(in, in, in, in, in, in, in, in, in, in, in, in, in, in, in, in).outType(new Nil<Iterable<double[]>>(){}).apply();
		assertArrayEquals(out.iterator().next(), expected.get(0), 0);
	}

}
