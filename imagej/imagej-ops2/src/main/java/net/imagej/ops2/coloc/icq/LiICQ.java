/*-
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
package net.imagej.ops2.coloc.icq;

import java.util.function.BiFunction;

import net.imagej.ops2.coloc.ColocUtil;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.IterablePair;
import net.imglib2.util.Pair;

import org.scijava.function.Computers;
import org.scijava.function.Functions;
import org.scijava.ops.spi.OpDependency;

/**
 * This algorithm calculates Li et al.'s ICQ (intensity correlation quotient).
 *
 * @param <T>
 *            Type of the first image
 * @param <U>
 *            Type of the second image
 *@implNote op names='coloc.icq'
 */
public class LiICQ<T extends RealType<T>, U extends RealType<U>, V extends RealType<V>>
		implements Functions.Arity4<Iterable<T>, Iterable<U>, DoubleType, DoubleType, Double> {

	@OpDependency(name = "stats.mean")
	private Computers.Arity1<Iterable<T>, DoubleType> meanTOp;
	@OpDependency(name = "stats.mean")
	private Computers.Arity1<Iterable<U>, DoubleType> meanUOp;

	/**
	 * TODO
	 *
	 * @param image1
	 * @param image2
	 * @param mean1
	 * @param mean2
	 * @return the output
	 */
	@Override
	public Double apply(final Iterable<T> image1, final Iterable<U> image2, final DoubleType mean1, final DoubleType mean2) {

		if (!ColocUtil.sameIterationOrder(image1, image2))
			throw new IllegalArgumentException(
					"Input and output must have the same dimensionality and iteration order!");

		final Iterable<Pair<T, U>> samples = new IterablePair<>(image1, image2);

		final double m1 = mean1 == null ? computeMeanTOf(image1) : mean1.get();
		final double m2 = mean2 == null ? computeMeanUOf(image2) : mean2.get();

		// variables to count the positive and negative results
		// of Li's product of the difference of means.
		long numPositiveProducts = 0;
		long numNegativeProducts = 0;
		// iterate over image
		for (final Pair<T, U> value : samples) {

			final double ch1 = value.getA().getRealDouble();
			final double ch2 = value.getB().getRealDouble();

			final double productOfDifferenceOfMeans = (m1 - ch1) * (m2 - ch2);

			// check for positive and negative values
			if (productOfDifferenceOfMeans < 0.0)
				++numNegativeProducts;
			else
				++numPositiveProducts;
		}

		/*
		 * calculate Li's ICQ value by dividing the amount of "positive pixels" to the
		 * total number of pixels. Then shift it in the -0.5,0.5 range.
		 */
		final double icqValue = (double) numPositiveProducts / (double) (numNegativeProducts + numPositiveProducts)
				- 0.5;
		return icqValue;
	}

	private double computeMeanTOf(final Iterable<T> in) {
		DoubleType mean = new DoubleType();
		meanTOp.compute(in, mean);
		return mean.get();
	}
	private double computeMeanUOf(final Iterable<U> in) {
		DoubleType mean = new DoubleType();
		meanUOp.compute(in, mean);
		return mean.get();
	}

}

/**
 *@implNote op names='coloc.icq'
 */
class LiICQSimple<T extends RealType<T>, U extends RealType<U>, V extends RealType<V>>
		implements BiFunction<Iterable<T>, Iterable<U>, Double> {
	
	@OpDependency(name = "coloc.icq")
	private Functions.Arity4<Iterable<T>, Iterable<U>, DoubleType, DoubleType, Double> colocOp;
	
	/**
	 * TODO
	 *
	 * @param image1
	 * @param image2
	 * @return the output
	 */
	@Override
	public Double apply(Iterable<T> image1, Iterable<U> image2) {
		return colocOp.apply(image1, image2, null, null);
	}

}
