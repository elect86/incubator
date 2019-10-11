/*
 * This is autogenerated source code -- DO NOT EDIT. Instead, edit the
 * corresponding template in templates/ and rerun bin/generate.groovy.
 */


package org.scijava.ops.function;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Container class for
 * higher-<a href="https://en.wikipedia.org/wiki/Arity">arity</a>
 * {@link Consumer}-style functional interfaces&mdash;i.e. with functional
 * method {@code accept} with a number of arguments corresponding to the arity.
 * <ul>
 * <li>For 1-arity (unary) consumers, use {@link Consumer}.</li>
 * <li>For 2-arity (binary) consumers, use {@link BiConsumer}.</li>
 * </ul>
 *
 * @author Curtis Rueden
 * @author Gabriel Selzer
 */
public final class Consumers {

	private Consumers() {
		// NB: Prevent instantiation of container class.
	}

	/**
	 * Represents an operation that accepts no input arguments and returns no
	 * result. This is the zero-arity specialization of {@link Consumer}. Unlike
	 * most other functional interfaces, this interface is expected to operate via
	 * side-effects.
	 * <p>
	 * This is a functional interface whose functional method is {@link \#accept(Object, Object)}.
	 * </p>
	 *
	 * @see Consumer
	 */
	@FunctionalInterface
	public interface Arity0 extends Runnable {

		/**
		 * Performs the operation.
		 */
		void accept();

		@Override
		default void run() {
			accept();
		}

		/**
		 * Returns a composed {@code Consumer.Arity0} that performs, in sequence, this
		 * operation followed by the {@code after} operation. If performing either
		 * operation throws an exception, it is relayed to the caller of the composed
		 * operation. If performing this operation throws an exception, the
		 * {@code after} operation will not be performed.
		 *
		 * @param after
		 *            the operation to perform after this operation
		 * @return a composed {@code Consumer.Arity0} that performs in sequence this
		 *         operation followed by the {@code after} operation
		 * @throws NullPointerException
		 *             if {@code after} is null
		 */
		default Arity0 andThen(Arity0 after) {
			Objects.requireNonNull(after);

			return () -> {
				accept();
				after.accept();
			};
		}
	}

	/**
	 * Represents an operation that accepts 3 input arguments and returns no
	 * result. This is the 3-arity specialization of {@link Consumer}. Unlike
	 * most other functional interfaces, this interface is expected to operate via
	 * side-effects.
	 * <p>
	 * This is a functional interface whose functional method is
	 * {@link \#accept(Object, Object, Object)}.
	 * </p>
	 *
	 * @param <I1>
	 *            the type of argument 1.
	 * @param <I2>
	 *            the type of argument 2.
	 * @param <I3>
	 *            the type of argument 3.
	 * @see Consumer
	 */
	@FunctionalInterface
	public interface Arity3<I1, I2, I3>{

		/**
		 * Performs this operation on the given arguments.
		 *
		 * @param in1
		 *            input argument 1.
		 * @param in2
		 *            input argument 2.
		 * @param in3
		 *            input argument 3.
		 */
		void accept(final I1 in1, final I2 in2, final I3 in3);

		/**
		 * Returns a composed {@code Consumer.Arity3} that performs, in sequence, this
		 * operation followed by the {@code after} operation. If performing either
		 * operation throws an exception, it is relayed to the caller of the composed
		 * operation. If performing this operation throws an exception, the
		 * {@code after} operation will not be performed.
		 *
		 * @param after
		 *            the operation to perform after this operation
		 * @return a composed {@code Consumer.Arity3} that performs in sequence this
		 *         operation followed by the {@code after} operation
		 * @throws NullPointerException
		 *             if {@code after} is null
		 */
		default Arity3<I1, I2, I3> andThen(Arity3<? super I1, ? super I2, ? super I3> after) {
			Objects.requireNonNull(after);

			return (in1, in2, in3) -> {
				accept(in1, in2, in3);
				after.accept(in1, in2, in3);
			};
		}
	}
	
	/**
	 * Represents an operation that accepts 4 input arguments and returns no
	 * result. This is the 4-arity specialization of {@link Consumer}. Unlike
	 * most other functional interfaces, this interface is expected to operate via
	 * side-effects.
	 * <p>
	 * This is a functional interface whose functional method is
	 * {@link \#accept(Object, Object, Object, Object)}.
	 * </p>
	 *
	 * @param <I1>
	 *            the type of argument 1.
	 * @param <I2>
	 *            the type of argument 2.
	 * @param <I3>
	 *            the type of argument 3.
	 * @param <I4>
	 *            the type of argument 4.
	 * @see Consumer
	 */
	@FunctionalInterface
	public interface Arity4<I1, I2, I3, I4>{

		/**
		 * Performs this operation on the given arguments.
		 *
		 * @param in1
		 *            input argument 1.
		 * @param in2
		 *            input argument 2.
		 * @param in3
		 *            input argument 3.
		 * @param in4
		 *            input argument 4.
		 */
		void accept(final I1 in1, final I2 in2, final I3 in3, final I4 in4);

		/**
		 * Returns a composed {@code Consumer.Arity4} that performs, in sequence, this
		 * operation followed by the {@code after} operation. If performing either
		 * operation throws an exception, it is relayed to the caller of the composed
		 * operation. If performing this operation throws an exception, the
		 * {@code after} operation will not be performed.
		 *
		 * @param after
		 *            the operation to perform after this operation
		 * @return a composed {@code Consumer.Arity4} that performs in sequence this
		 *         operation followed by the {@code after} operation
		 * @throws NullPointerException
		 *             if {@code after} is null
		 */
		default Arity4<I1, I2, I3, I4> andThen(Arity4<? super I1, ? super I2, ? super I3, ? super I4> after) {
			Objects.requireNonNull(after);

			return (in1, in2, in3, in4) -> {
				accept(in1, in2, in3, in4);
				after.accept(in1, in2, in3, in4);
			};
		}
	}
	
	/**
	 * Represents an operation that accepts 5 input arguments and returns no
	 * result. This is the 5-arity specialization of {@link Consumer}. Unlike
	 * most other functional interfaces, this interface is expected to operate via
	 * side-effects.
	 * <p>
	 * This is a functional interface whose functional method is
	 * {@link \#accept(Object, Object, Object, Object, Object)}.
	 * </p>
	 *
	 * @param <I1>
	 *            the type of argument 1.
	 * @param <I2>
	 *            the type of argument 2.
	 * @param <I3>
	 *            the type of argument 3.
	 * @param <I4>
	 *            the type of argument 4.
	 * @param <I5>
	 *            the type of argument 5.
	 * @see Consumer
	 */
	@FunctionalInterface
	public interface Arity5<I1, I2, I3, I4, I5>{

		/**
		 * Performs this operation on the given arguments.
		 *
		 * @param in1
		 *            input argument 1.
		 * @param in2
		 *            input argument 2.
		 * @param in3
		 *            input argument 3.
		 * @param in4
		 *            input argument 4.
		 * @param in5
		 *            input argument 5.
		 */
		void accept(final I1 in1, final I2 in2, final I3 in3, final I4 in4, final I5 in5);

		/**
		 * Returns a composed {@code Consumer.Arity5} that performs, in sequence, this
		 * operation followed by the {@code after} operation. If performing either
		 * operation throws an exception, it is relayed to the caller of the composed
		 * operation. If performing this operation throws an exception, the
		 * {@code after} operation will not be performed.
		 *
		 * @param after
		 *            the operation to perform after this operation
		 * @return a composed {@code Consumer.Arity5} that performs in sequence this
		 *         operation followed by the {@code after} operation
		 * @throws NullPointerException
		 *             if {@code after} is null
		 */
		default Arity5<I1, I2, I3, I4, I5> andThen(Arity5<? super I1, ? super I2, ? super I3, ? super I4, ? super I5> after) {
			Objects.requireNonNull(after);

			return (in1, in2, in3, in4, in5) -> {
				accept(in1, in2, in3, in4, in5);
				after.accept(in1, in2, in3, in4, in5);
			};
		}
	}
	
}
