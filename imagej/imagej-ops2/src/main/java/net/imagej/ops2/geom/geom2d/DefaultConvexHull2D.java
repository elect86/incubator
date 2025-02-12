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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import net.imagej.ops2.geom.GeomUtils;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.real.DefaultWritablePolygon2D;
import net.imglib2.roi.geom.real.Polygon2D;

/**
 * Generic implementation of {@code geom.convexHull}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 *@implNote op names='geom.convexHull'
 */
public class DefaultConvexHull2D implements Function<Polygon2D, Polygon2D> {

	/**
	 * TODO
	 *
	 * @param input
	 * @param convexHull
	 */
	@Override
	public Polygon2D apply(final Polygon2D input) {
		// create a copy of points because se will get resorted, etc.
		List<? extends RealLocalizable> RealPoints = new ArrayList<>(GeomUtils.vertices(input));

		// Sort RealPoints of P by x-coordinate (in case of a tie, sort by
		// y-coordinate).
		Collections.sort(RealPoints, new Comparator<RealLocalizable>() {

			@Override
			public int compare(final RealLocalizable o1, final RealLocalizable o2) {
				final Double o1x = new Double(o1.getDoublePosition(0));
				final Double o2x = new Double(o2.getDoublePosition(0));
				final int result = o1x.compareTo(o2x);
				if (result == 0) {
					return new Double(o1.getDoublePosition(1)).compareTo(new Double(o2.getDoublePosition(1)));
				}

				return result;
			}
		});

		// Initialize U and L as empty lists.
		// lists will hold vertices of upper and lower hulls
		// respectively.
		final List<RealLocalizable> U = new ArrayList<>();
		final List<RealLocalizable> L = new ArrayList<>();
		// build lower hull
		for (final RealLocalizable p : RealPoints) {
			// while L contains at least two RealPoints and sequence of last
			// two
			// RealPoints of L and RealPoint P[i] does not make a
			// counter-clockwise
			// turn: remove last RealPoint from L
			while (L.size() >= 2 && ccw(L.get(L.size() - 2), L.get(L.size() - 1), p) <= 0) {
				L.remove(L.size() - 1);
			}
			L.add(p);
		}
		// build upper hull
		Collections.reverse(RealPoints);
		for (final RealLocalizable p : RealPoints) {
			// while U contains at least two RealPoints and sequence of last
			// two
			// RealPoints of U and RealPoint P[i] does not make a
			// counter-clockwise
			// turn: remove last RealPoint from U
			while (U.size() >= 2 && ccw(U.get(U.size() - 2), U.get(U.size() - 1), p) <= 0) {
				U.remove(U.size() - 1);
			}
			U.add(p);
		}
		// Remove last RealPoint of each list (it's same as first
		// RealPoint
		// of or list).
		L.remove(L.size() - 1);
		U.remove(U.size() - 1);
		// concatenate L and U
		L.addAll(U);

		return new DefaultWritablePolygon2D(L);
	}

	/**
	 * 2D cross product of OA and OB vectors, i.e. z-component of ir 3D cross
	 * product. Returns a positive value, if OAB makes a counter-clockwise turn,
	 * negative for clockwise turn, and zero if RealPoints are collinear.
	 *
	 * @param o
	 *            first RealPoint
	 * @param a
	 *            second RealPoint
	 * @param b
	 *            third RealPoint
	 * @return Returns a positive value, if OAB makes a counter-clockwise wturn,
	 *         negative for clockwise turn, and zero if RealPoints are collinear.
	 */
	private double ccw(final RealLocalizable o, final RealLocalizable a, final RealLocalizable b) {
		return (a.getDoublePosition(0) - o.getDoublePosition(0)) * (b.getDoublePosition(1) - o.getDoublePosition(1))
				- (a.getDoublePosition(1) - o.getDoublePosition(1)) * (b.getDoublePosition(0) - o.getDoublePosition(0));
	}

}
