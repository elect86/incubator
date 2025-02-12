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

package net.imagej.ops2.copy;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.util.Intervals;

import org.scijava.function.Computers;
import org.scijava.ops.spi.OpDependency;

/**
 * Copies a {@link RandomAccessibleInterval} into another
 * {@link RandomAccessibleInterval}
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 * @implNote op names='copy, copy.rai', priority='1.0'
 */
public class CopyRAI<T> implements Computers.Arity1<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> {

	@OpDependency(name = "copy")
	private Computers.Arity1<T, T> mapComputer;

	/**
	 * TODO
	 *
	 * @param input
	 * @param copy
	 */
	@Override
	public void compute(final RandomAccessibleInterval<T> input, final RandomAccessibleInterval<T> output) {
		if (!Intervals.equalDimensions(input, output))
			throw new IllegalArgumentException("input and output must be of the same dimensionality!");
		LoopBuilder.setImages(input, output).forEachPixel((in, out) -> mapComputer.compute(in, out));
	}
}
