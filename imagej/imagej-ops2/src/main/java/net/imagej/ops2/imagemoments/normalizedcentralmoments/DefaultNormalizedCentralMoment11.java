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

package net.imagej.ops2.imagemoments.normalizedcentralmoments;

import net.imagej.ops2.imagemoments.AbstractImageMomentOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.function.Computers;
import org.scijava.ops.spi.Op;
import org.scijava.ops.spi.OpDependency;

/**
 * Op to calculate the {@code imageMoments.normalizedCentralMoment11}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <I> input type
 * @param <O> output type
 * @implNote op names='imageMoments.normalizedCentralMoment11', label='Image Moment: NormalizedCentralMoment11'
 */
public class DefaultNormalizedCentralMoment11<I extends RealType<I>, O extends RealType<O>>
	implements AbstractImageMomentOp<I, O> 
{

	@OpDependency(name = "imageMoments.centralMoment00")
	private Computers.Arity1<RandomAccessibleInterval<I>, O> centralMoment00Func;

	@OpDependency(name = "imageMoments.centralMoment11")
	private Computers.Arity1<RandomAccessibleInterval<I>, O> centralMoment11Func;

	@Override
	public void computeMoment(final RandomAccessibleInterval<I> input, final O output) {
		final O moment00 = output.createVariable();
		centralMoment00Func.compute(input, moment00);
		final O moment11 = output.createVariable();
		centralMoment11Func.compute(input, moment11);

		output.setReal(moment11.getRealDouble() /
			Math.pow(moment00.getRealDouble(), 1 + ((1 + 1) / 2.0)));
	}
}
