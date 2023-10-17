/*-
 * #%L
 * SciJava Operations Engine: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2016 - 2023 SciJava developers.
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

package org.scijava.ops.engine;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.scijava.ops.api.InfoTree;
import org.scijava.ops.api.OpInfo;

public interface InfoTreeGenerator {


	/**
	 * Generates an {@link InfoTree}. This {@link InfoTreeGenerator} is only
	 * responsible for generating the <b>outer layer</b> of the {@link InfoTree},
	 * and delegates to {@code generators} to
	 * 
	 * @param signature the signature for which an {@link InfoTreeGenerator} must be generated
	 * @param idMap the available {@link OpInfo}s, keyed by their id
	 * @param generators the available {@link InfoTreeGenerator}s
	 * @return an {@link InfoTree} matching the specifications of
	 *         {@code signature}
	 */
	InfoTree generate(String signature, Map<String, OpInfo> idMap,
		Collection<InfoTreeGenerator> generators);

	/**
	 * Filters through a list of {@link InfoTreeGenerator}s to find the generator
	 * best suited towards generating {@code signature}
	 * <p>
	 * 
	 * @param signature the signature that must be generated
	 * @param generators the list of {@link InfoTreeGenerator}s
	 * @return the {@link InfoTreeGenerator} best suited to the task
	 */
	static InfoTreeGenerator findSuitableGenerator(String signature,
		Collection<InfoTreeGenerator> generators)
	{
		List<InfoTreeGenerator> suitableGenerators = generators.stream() //
			.filter(g -> g.canGenerate(signature)) //
			.collect(Collectors.toList());
		if (suitableGenerators.size() == 0) throw new IllegalArgumentException(
			"No InfoTreeGenerator in given collection " + generators +
				" is able to reify signature " + signature);
		if (suitableGenerators.size() > 1) throw new IllegalArgumentException(
			"Signature " + signature +
				" is able to be reified by multiple generators: " + suitableGenerators);
		return suitableGenerators.get(0);
	}

	static InfoTree generateDependencyTree(String subsignature,
		Map<String, OpInfo> idMap, Collection<InfoTreeGenerator> generators)
	{
		InfoTreeGenerator genOpt = InfoTreeGenerator
			.findSuitableGenerator(subsignature, generators);
		return genOpt.generate(subsignature, idMap, generators);
	}

	/**
	 * Finds the subsignature in {@link String} {@code signature}. The
	 * subsignature is assumed to start at index {@code start}.
	 * 
	 * @param signature
	 * @param start
	 * @return a signature contained withing {@code signature}
	 */
	static String subSignatureFrom(String signature, int start) {
		int depsStart = signature.indexOf(InfoTree.DEP_START_DELIM, start);
		int depth = 0;
		for (int i = depsStart; i < signature.length(); i++) {
			char ch = signature.charAt(i);
			if (ch == InfoTree.DEP_START_DELIM) depth++;
			else if (ch == InfoTree.DEP_END_DELIM) {
				depth--;
				if (depth == 0) {
					int depsEnd = i;
					return signature.substring(start, depsEnd + 1);
				}
			}
		}
		throw new IllegalArgumentException(
			"There is no complete signature starting from index " + start +
				" in signature " + signature);
	}

	/**
	 * Describes whether this {@link InfoTreeGenerator} is designed to generate
	 * the <b>outer layer</b> of the {@link InfoTree}
	 *
	 * @param signature the signature to use as the template for the
	 *          {@link InfoTree}
	 * @return true iff this {@link InfoTreeGenerator} can generate the outer
	 *         layer of the {@link InfoTree}
	 */
	boolean canGenerate(String signature);

}