/*-
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
open module net.imagej.ops2 {
	exports net.imagej.ops2;

	requires java.scripting;
	requires net.imagej.mesh2;
	requires net.imglib2;
	requires net.imglib2.algorithm;
	requires net.imglib2.algorithm.fft2;
	requires net.imglib2.roi;
	requires org.joml;
	requires org.scijava.collections;
	requires org.scijava.concurrent;
	requires org.scijava.function;
	requires org.scijava.meta;
	requires org.scijava.ops.api;
	requires org.scijava.ops.spi;
	requires org.scijava.priority;
	requires org.scijava.types;
	
	// FIXME: these module names derive from filenames and are thus unstable
	requires commons.math3;
	requires ojalgo;
	requires jama;
	requires mines.jtk;
	requires net.imglib2.realtransform;

	provides org.scijava.types.TypeExtractor with
			net.imagej.ops2.types.ImgFactoryTypeExtractor,
			net.imagej.ops2.types.ImgLabelingTypeExtractor,
			net.imagej.ops2.types.NativeImgTypeExtractor,
			net.imagej.ops2.types.LabelingMappingTypeExtractor,
			net.imagej.ops2.types.OutOfBoundsConstantValueFactoryTypeExtractor,
			net.imagej.ops2.types.OutOfBoundsFactoryTypeExtractor,
			net.imagej.ops2.types.OutOfBoundsRandomValueFactoryTypeExtractor,
			net.imagej.ops2.types.RAITypeExtractor;

}
