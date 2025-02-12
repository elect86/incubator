
package org.scijava.ops.engine.yaml.ops;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * A final {@link Field}, exposed to Ops via YAML
 * 
 * @author Gabriel Selzer
 */
public class YAMLFieldOp {

	public final BiFunction<Double, Double, Double> multiply = (a, b) -> a * b;

}
