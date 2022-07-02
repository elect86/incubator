/*
 * #%L
 * SciJava Operations: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2016 - 2019 SciJava Ops developers.
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

/*
* This is autogenerated source code -- DO NOT EDIT. Instead, edit the
* corresponding template in templates/ and rerun bin/generate.groovy.
*/

package org.scijava.ops.engine.adapt.lift;

import java.util.function.Function;

import org.scijava.function.Computers;
import org.scijava.ops.spi.OpCollection;
import org.scijava.ops.spi.OpField;

/**
 * Collection of ops designed to lift {@link Computers} to operate
 * on arrays. TODO: multi-threading support
 * 
 * @author Gabriel Selzer
 */
public class ComputerToArrays<I, I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, I12, I13, I14, I15, I16, O> implements OpCollection {

	private int minLength(Object[]... arrays) {
		int minLength = Integer.MAX_VALUE;
		for (Object[] array : arrays)
			if (array.length < minLength) minLength = array.length;
		return minLength;
	}

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity0<O>, Computers.Arity0<O[]>> liftComputer0 =
		(computer) -> {
			return (out) -> {
				int max = minLength(out);
				for (int i = 0; i < max; i++) {
					computer.compute(out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity1<I, O>, Computers.Arity1<I[], O[]>> liftComputer1 =
		(computer) -> {
			return (in, out) -> {
				int max = minLength(in, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity2<I1, I2, O>, Computers.Arity2<I1[], I2[], O[]>> liftComputer2 =
		(computer) -> {
			return (in1, in2, out) -> {
				int max = minLength(in1, in2, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity3<I1, I2, I3, O>, Computers.Arity3<I1[], I2[], I3[], O[]>> liftComputer3 =
		(computer) -> {
			return (in1, in2, in3, out) -> {
				int max = minLength(in1, in2, in3, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity4<I1, I2, I3, I4, O>, Computers.Arity4<I1[], I2[], I3[], I4[], O[]>> liftComputer4 =
		(computer) -> {
			return (in1, in2, in3, in4, out) -> {
				int max = minLength(in1, in2, in3, in4, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity5<I1, I2, I3, I4, I5, O>, Computers.Arity5<I1[], I2[], I3[], I4[], I5[], O[]>> liftComputer5 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity6<I1, I2, I3, I4, I5, I6, O>, Computers.Arity6<I1[], I2[], I3[], I4[], I5[], I6[], O[]>> liftComputer6 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity7<I1, I2, I3, I4, I5, I6, I7, O>, Computers.Arity7<I1[], I2[], I3[], I4[], I5[], I6[], I7[], O[]>> liftComputer7 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity8<I1, I2, I3, I4, I5, I6, I7, I8, O>, Computers.Arity8<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], O[]>> liftComputer8 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity9<I1, I2, I3, I4, I5, I6, I7, I8, I9, O>, Computers.Arity9<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], O[]>> liftComputer9 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity10<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, O>, Computers.Arity10<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], O[]>> liftComputer10 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity11<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, O>, Computers.Arity11<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], I11[], O[]>> liftComputer11 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], in11[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity12<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, I12, O>, Computers.Arity12<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], I11[], I12[], O[]>> liftComputer12 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], in11[i], in12[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity13<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, I12, I13, O>, Computers.Arity13<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], I11[], I12[], I13[], O[]>> liftComputer13 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], in11[i], in12[i], in13[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity14<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, I12, I13, I14, O>, Computers.Arity14<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], I11[], I12[], I13[], I14[], O[]>> liftComputer14 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], in11[i], in12[i], in13[i], in14[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity15<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, I12, I13, I14, I15, O>, Computers.Arity15<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], I11[], I12[], I13[], I14[], I15[], O[]>> liftComputer15 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14, in15, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14, in15, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], in11[i], in12[i], in13[i], in14[i], in15[i], out[i]);
				}
			};
		};

	@OpField(names = "adapt", params = "fromOp, toOp")
	public final Function<Computers.Arity16<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, I11, I12, I13, I14, I15, I16, O>, Computers.Arity16<I1[], I2[], I3[], I4[], I5[], I6[], I7[], I8[], I9[], I10[], I11[], I12[], I13[], I14[], I15[], I16[], O[]>> liftComputer16 =
		(computer) -> {
			return (in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14, in15, in16, out) -> {
				int max = minLength(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14, in15, in16, out);
				for (int i = 0; i < max; i++) {
					computer.compute(in1[i], in2[i], in3[i], in4[i], in5[i], in6[i], in7[i], in8[i], in9[i], in10[i], in11[i], in12[i], in13[i], in14[i], in15[i], in16[i], out[i]);
				}
			};
		};

}
