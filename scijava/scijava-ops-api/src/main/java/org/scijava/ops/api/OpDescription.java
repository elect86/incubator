package org.scijava.ops.api;

import org.scijava.struct.Member;

/**
 * Static utility class for formatting descriptions of Ops
 */
public final class OpDescription {

    /**
     * Generates a basic {@link String} describing the given {@link OpInfo}
     *
     * @param info the {@link OpInfo} of interest
     * @return a descriptor for this {@link OpInfo}
     */
    public static String basic(final OpInfo info) {
        return basic(info, null);
    }

    /**
     * Writes a basic {@link String} describing the {@link OpInfo} of interest
     * <b>with a particular {@link Member} highlighted</b>.
     *
     * @param info the {@link OpInfo} of interest
     * @param special the {@link Member} to highlight
     * @return a descriptor for this {@link OpInfo}
     */
    public static String basic(final OpInfo info, final Member<?> special) {
        final StringBuilder sb = new StringBuilder();
        sb.append(info.implementationName()).append("(\n\t Inputs:\n");
        for (final Member<?> arg : info.inputs()) {
            appendParam(sb, arg, special);
        }
        sb.append("\t Outputs:\n");
        appendParam(sb, info.output(), special);
        sb.append(")\n");
        return sb.toString();
    }

    /**
     * Appends a {@link Member} to the {@link StringBuilder} writing the Op
     * string.
     *
     * @param sb      the {@link StringBuilder}
     * @param arg     the {@link Member} being appended to {@code sb}
     * @param special the {@link Member} to highlight
     */
    private static void appendParam(final StringBuilder sb, final Member<?> arg,
                             final Member<?> special) {
        if (arg == special) sb.append("==> \t"); // highlight special item
        else sb.append("\t\t");
        sb.append(arg.getType().getTypeName());
        sb.append(" ");
        sb.append(arg.getKey());
        if (!arg.getDescription().isEmpty()) {
            sb.append(" -> ");
            sb.append(arg.getDescription());
        }
        sb.append("\n");
    }
}