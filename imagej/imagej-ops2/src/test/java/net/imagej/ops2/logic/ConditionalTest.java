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

package net.imagej.ops2.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.imagej.ops2.AbstractOpTest;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Ternary} and {@link Default}.
 *
 * @author Leon Yang
 */
public class ConditionalTest extends AbstractOpTest {

	@Test
	public void testIf() {
		final ByteType ifTrueVal = new ByteType((byte) 10);
		final ByteType ifFalseVal = new ByteType((byte) 100);
		final ByteType outVal = new ByteType();
		ops.op("logic.match").input(new BoolType(true), ifTrueVal, ifFalseVal).output(outVal).compute();
		assertEquals(10, outVal.get());
		ops.op("logic.match").input(new BoolType(false), ifTrueVal, ifFalseVal).output(outVal).compute();
		assertEquals(100, outVal.get());
	}

	@Test
	public void testDefault() {
		final ByteType out = new ByteType((byte) 10);
		final ByteType defaultVal = new ByteType((byte) 100);
		ops.op("logic.default").input(new BoolType(true), defaultVal).output(out).compute();
		assertEquals(10, out.get());
		ops.op("logic.default").input(new BoolType(false), defaultVal).output(out).compute();
		assertEquals(100, out.get());
	}

}
