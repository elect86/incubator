
package org.scijava.ops.api;

/**
 * Describes the execution of an Op
 *
 * @author Gabe Selzer
 */
public class OpExecutionSummary {

	/** The Op executed */
	private final RichOp op;

	/**
	 * The {@link Object} produced by this execution of {@code op}
	 */
	private final Object output;

	public OpExecutionSummary(RichOp op, Object output)
	{
		this.op = op;
		this.output = output;
	}

	public RichOp op() {
		return op;
	}

	/**
	 * Returns the output {@link Object} of this execution
	 *
	 * @return the output of the execution
	 */
	public Object output() {
		return output;
	}

	/**
	 * Describes whether {@code o} is the output of this
	 * {@link OpExecutionSummary}
	 *
	 * @param o the {@link Object} that might be {@link OpExecutionSummary#output}
	 * @return true iff {@code o == output}
	 */
	public boolean isOutput(Object o) {
		return output == o;
	}

}
