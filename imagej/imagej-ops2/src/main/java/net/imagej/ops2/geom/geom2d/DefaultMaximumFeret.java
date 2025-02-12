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

package net.imagej.ops2.geom.geom2d;

import java.util.List;
import java.util.function.Function;

import net.imagej.ops2.geom.GeomUtils;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.ops.spi.OpDependency;

/**
 * Maximum Feret of a polygon.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *@implNote op names='geom.maximumFeret'
 */
public class DefaultMaximumFeret implements Function<Polygon2D, Pair<RealLocalizable, RealLocalizable>> {

	@OpDependency(name = "geom.convexHull")
	private Function<Polygon2D, Polygon2D> function;

	/**
	 * TODO
	 *
	 * @param input
	 * @param maximumFeret
	 */
	@Override
	public Pair<RealLocalizable, RealLocalizable> apply(Polygon2D input) {
		final List<? extends RealLocalizable> points = GeomUtils.vertices(function.apply(input));

		double distance = Double.NEGATIVE_INFINITY;
		RealLocalizable p0 = points.get(0);
		RealLocalizable p1 = points.get(0);
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 2; j < points.size(); j++) {
				final RealLocalizable tmpP0 = points.get(i);
				final RealLocalizable tmpP1 = points.get(j);

				final double tmp = Math.sqrt(Math.pow(tmpP0.getDoublePosition(0) - tmpP1.getDoublePosition(0), 2)
						+ Math.pow(tmpP0.getDoublePosition(1) - tmpP1.getDoublePosition(1), 2));

				if (tmp > distance) {
					distance = tmp;
					p0 = tmpP0;
					p1 = tmpP1;
				}
			}
		}

		return new ValuePair<>(p0, p1);
	}

}
