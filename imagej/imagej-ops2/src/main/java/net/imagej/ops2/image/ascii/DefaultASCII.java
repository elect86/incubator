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

package net.imagej.ops2.image.ascii;

import java.util.function.Function;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.function.Functions;
import org.scijava.ops.spi.OpDependency;

/**
 * Generates an ASCII version of an image.
 * <p>
 * Only the first two dimensions of the image are considered.
 * </p>
 * 
 * @author Curtis Rueden
 *@implNote op names='image.ascii'
 */
public class DefaultASCII<T extends RealType<T>> implements Functions.Arity3<IterableInterval<T>, T, T, String>
{

	private static final String CHARS = "#O*o+-,. ";

	@OpDependency(name = "stats.minMax")
	private Function<IterableInterval<T>, Pair<T, T>> minMaxFunc;

	/**
	 * TODO
	 *
	 * @param iterableInput
	 * @param min
	 * @param max
	 * @param ASCIIArt
	 */
	@Override
	public String apply(final IterableInterval<T> input, T min, T max) {
		if (min == null || max == null) {
			final Pair<T, T> minMax = minMaxFunc.apply(input);
			if (min == null) min = minMax.getA();
			if (max == null) max = minMax.getB();
		}
		return ascii(input, min, max);
	}

	// -- Utility methods --

	public static <T extends RealType<T>> String ascii(
		final IterableInterval<T> image, final T min, final T max)
	{
		final long dim0 = image.dimension(0);
		final long dim1 = image.dimension(1);
		// TODO: Check bounds.
		final int w = (int) (dim0 + 1);
		final int h = (int) dim1;

		// span = max - min
		final T span = max.copy();
		span.sub(min);

		// allocate ASCII character array
		final char[] c = new char[w * h];
		for (int y = 1; y <= h; y++) {
			c[w * y - 1] = '\n'; // end of row
		}

		// loop over all available positions
		final Cursor<T> cursor = image.localizingCursor();
		final int[] pos = new int[image.numDimensions()];
		final T tmp = image.firstElement().copy();
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(pos);
			final int index = w * pos[1] + pos[0];

			// normalized = (value - min) / (max - min)
			tmp.set(cursor.get());
			tmp.sub(min);
			final double normalized = tmp.getRealDouble() / span.getRealDouble();

			final int charLen = CHARS.length();
			final int charIndex = (int) (charLen * normalized);
			c[index] = CHARS.charAt(charIndex < charLen ? charIndex : charLen - 1);
		}

		return new String(c);
	}

}
