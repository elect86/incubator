/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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
package net.imagej.ops2.transform.flatIterableView;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import net.imagej.ops2.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.junit.jupiter.api.Test;
import org.scijava.ops.api.OpBuilder;
import org.scijava.types.Nil;

/**
 * Tests {@link net.imagej.ops2.Ops.Transform.FlatIterableView} ops.
 * <p>
 * This test only checks if the op call works with all parameters and that the
 * result is equal to that of the {@link Views} method call. It is not a
 * correctness test of {@link Views} itself.
 * </p>
 *
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
public class FlatIterableViewTest extends AbstractOpTest {
	Nil<RandomAccessibleInterval<DoubleType>> raiNil = new Nil<RandomAccessibleInterval<DoubleType>>() {
	};
	Nil<IterableInterval<DoubleType>> iiNil = new Nil<IterableInterval<DoubleType>>() {
	};

	@Test
	public void defaultFlatIterableTest() {
		Function<RandomAccessibleInterval<DoubleType>, IterableInterval<DoubleType>> flatIterableFunc = OpBuilder
				.matchFunction(ops, "transform.flatIterableView", raiNil, iiNil);

		Img<DoubleType> img = new ArrayImgFactory<>(new DoubleType()).create(new int[] { 10, 10 });

		Cursor<DoubleType> il2 = Views.flatIterable(img).cursor();

		Cursor<DoubleType> opr = flatIterableFunc.apply(img).cursor();

		while (il2.hasNext()) {
			il2.next();
			opr.next();
			assertEquals(il2.getDoublePosition(0), opr.getDoublePosition(0), 1e-10);
			assertEquals(il2.getDoublePosition(1), opr.getDoublePosition(1), 1e-10);
		}
	}
}
