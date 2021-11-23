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

package org.scijava.ops.engine.adapt.lift;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scijava.function.Functions;
import org.scijava.ops.engine.AbstractTestEnvironment;
import org.scijava.ops.engine.OpBuilderTestOps;
import org.scijava.types.Nil;

/**
 * Tests the adaptation of {@link Functions} running on a type into
 * {@link Functions} running on an {@link Iterable} of that type.
 * 
 * @author Gabriel Selzer
 */
public class FunctionToIterablesTest extends AbstractTestEnvironment {

	@BeforeClass
	public static void AddNeededOps() {
		discoverer.register(new FunctionToIterables());
		discoverer.register(new OpBuilderTestOps());
	}

	@Test
	public void testFunction1ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(1., 2., 3.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction2ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(2., 4., 6.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction3ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(3., 6., 9.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction4ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(4., 8., 12.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction5ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(5., 10., 15.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction6ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(6., 12., 18.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction7ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(7., 14., 21.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction8ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(8., 16., 24.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction9ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(9., 18., 27.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction10ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(10., 20., 30.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction11ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(11., 22., 33.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction12ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(12., 24., 36.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction13ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(13., 26., 39.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction14ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(14., 28., 42.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction15ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(15., 30., 45.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

	@Test
	public void testFunction16ToIterables() {
		List<Double> in = Arrays.asList(1., 2., 3.);
		List<Double> expected = Arrays.asList(16., 32., 48.);
		Iterable<Double> output = ops.op("test.addDoubles") //
			.input(in, in, in, in, in, in, in, in, in, in, in, in, in, in, in, in) //
			.outType(new Nil<Iterable<Double>>()
			{}).apply();
		assertIterationsEqual(expected, output);
	}

}
