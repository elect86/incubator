
package org.scijava.ops.engine.impl;

import java.util.Objects;

import org.scijava.ops.api.Hints;
import org.scijava.ops.api.OpRef;

public class MatchingConditions {

	private final OpRef ref;
	private final Hints hints;

	private MatchingConditions(OpRef ref, Hints hints) {
		this.ref = ref;
		this.hints = hints;
	}

	public static MatchingConditions from(OpRef r, Hints h, boolean generateMatchingID) {
		return new MatchingConditions(r, h.copy(generateMatchingID));
	}

	public OpRef ref() {
		return ref;
	}

	public Hints hints() {
		return hints;
	}

	@Override
	public boolean equals(Object that) {
		if (!(that instanceof MatchingConditions)) return false;
		MatchingConditions thoseConditions = (MatchingConditions) that;
		return ref().equals(thoseConditions.ref()) && hints().equals(
			thoseConditions.hints());
	}

	@Override
	public int hashCode() {
		return Objects.hash(ref(), hints());
	}

}
