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

package org.scijava.types.extractors;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import org.scijava.priority.Priority;
import org.scijava.types.TypeExtractor;
import org.scijava.types.TypeReifier;

/**
 * {@link TypeExtractor} plugin which operates on {@link Map} objects.
 * <p>
 * For performance reasons, we examine only one entry of the map, which may be
 * more specifically typed than later entries. Hence the generic types given by
 * this extraction may be overly constrained.
 * </p>
 *
 * @author Curtis Rueden
 */
public class MapTypeExtractor implements TypeExtractor<Map<?, ?>> {

	@Override
	public Type reify(final TypeReifier t, final Map<?, ?> o, final int n) {
		if (n < 0 || n > 1) throw new IndexOutOfBoundsException("" + n);

		if (o.isEmpty()) return null;

		final Entry<?, ?> entry = o.entrySet().iterator().next();
		if (n == 0) return t.reify(entry.getKey());
		return t.reify(entry.getValue());

		// TODO: Avoid infinite recursion when the map references itself.
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class<Map<?, ?>> getRawType() {
		return (Class) Map.class;
	}

	@Override
	public double priority() {
		return Priority.LOW;
	}

}
