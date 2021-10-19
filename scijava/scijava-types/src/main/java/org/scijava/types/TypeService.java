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

package org.scijava.types;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.scijava.plugin.SingletonService;
import org.scijava.service.SciJavaService;
import org.scijava.types.extractors.IterableTypeExtractor;

/**
 * Service for working with generic {@link Type}s. This service can extract
 * generic types from objects in an extensible way, via {@link TypeExtractor}
 * plugins. It also provides some methods for reasoning about generic types in
 * general.
 * 
 * @author Curtis Rueden
 */
public interface TypeService extends
	SingletonService<TypeExtractor<?>>, SciJavaService
{

	TypeReifier reifier();

	/** Gets the type extractor which handles the given class, or null if none. */
	default <T> TypeExtractor<T> getExtractor(Class<T> c) {
		return reifier().getExtractor(c);
	}

	/**
	 * Extracts the generic {@link Type} of the given {@link Object}.
	 * <p>
	 * The ideal goal of the extraction is to reconstitute a fully concrete
	 * generic type, with all type variables fully resolved&mdash;e.g.:
	 * {@code ArrayList<Integer>} rather than a raw {@code ArrayList} class or
	 * even {@code ArrayList<N extends Number>}. Failing that, though, type
	 * variables which are still unknown after analysis will be replaced with
	 * wildcards&mdash;e.g., {@code HashMap} might become
	 * {@code HashMap<String, ?>} if a concrete type for the map values cannot be
	 * determined.
	 * </p>
	 * <p>
	 * For objects whose concrete type has no parameters, this method simply
	 * returns {@code o.getClass()}. For example:
	 * 
	 * <pre>
	 *      StringList implements List&lt;String&gt;
	 * </pre>
	 * 
	 * will return {@code StringList.class}.
	 * <p>
	 * The interesting case is for objects whose concrete class <em>does</em> have
	 * type parameters. E.g.:
	 * 
	 * <pre>
	 *      NumberList&lt;N extends Number&gt; implements List&lt;N&gt;
	 *      ListMap&lt;K, V, T&gt; implements Map&lt;K, V&gt;, List&lt;T&gt;
	 * </pre>
	 * 
	 * For such types, we try to fill the type parameters recursively, using
	 * {@link TypeExtractor} plugins that know how to glean types at runtime from
	 * specific sorts of objects.
	 * </p>
	 * <p>
	 * For example, {@link IterableTypeExtractor} knows how to guess a {@code T}
	 * for any {@code Iterable<T>} by examining the type of the elements in its
	 * iteration. (Of course, this may be inaccurate if the elements in the
	 * iteration are heterogeneously typed, but for many use cases this guess is
	 * better than nothing.)
	 * </p>
	 * <p>
	 * In this way, the behavior of the generic type extraction is fully
	 * extensible, since additional {@link TypeExtractor} plugins can always be
	 * introduced which extract types more intelligently in cases where more
	 * <em>a priori</em> knowledge about that type is available at runtime.
	 * </p>
	 */
	default Type reify(final Object o) {
		return reifier().reify(o);
	}

	/**
	 * Extracts the resolved type variables of the given {@link Object}, as viewed
	 * through the specified supertype.
	 * <p>
	 * For example, if you call:
	 * </p>
	 * 
	 * <pre>
	 * args(Collections.singleton("Hi"), Iterable.class)
	 * </pre>
	 * <p>
	 * Then it returns a map with contents <code>{T: String}</code> by using the
	 * {@link IterableTypeExtractor} to analyze the object.
	 * </p>
	 * <p>
	 * Note that this method only provides results if there is a
	 * {@link TypeExtractor} plugin which handles <em>exactly</em> the given
	 * supertype.
	 * </p>
	 * 
	 * @see #reify(Object)
	 */
	default <T> Map<TypeVariable<?>, Type> args(final Object o,
		final Class<T> superType)
	{
		return reifier().args(o, superType);
	}

	// -- PTService methods --

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default Class<TypeExtractor<?>> getPluginType() {
		return (Class) TypeExtractor.class;
	}

}
