
package org.scijava.types.inference;

import com.google.common.base.Objects;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.scijava.types.Any;
import org.scijava.types.Nil;
import org.scijava.types.Types;

public class GenericAssignability {

	/**
	 * @param src the type for which assignment should be checked from
	 * @param dest the parameterized type for which assignment should be checked
	 *          to
	 * @param safeAssignability used to determine if we want to check if the
	 *          src->dest assignment would be safely assignable even though it
	 *          would cause a compiler error if we explicitly tried to do this
	 *          (useful pretty much only for Op matching)
	 * @return whether and assignment of source to destination would be a legal
	 *         java statement
	 */
	public static boolean checkGenericAssignability(Type src,
		ParameterizedType dest, boolean safeAssignability)
	{
		return GenericAssignability.checkGenericAssignability(src, dest, null,
			safeAssignability);
	}

	/**
	 * Checks whether it would be legal to assign the {@link Type} source to the
	 * specified {@link ParameterizedType} destination (which could possibly be a
	 * supertype of the source type). Thereby, possible {@link TypeVariable}s
	 * contained in the parameters of the source are tried to be inferred in the
	 * sense of empty angle brackets when a new object is created:
	 *
	 * <pre>
	 * 
	 * List&lt;Integer&gt; listOfInts = new ArrayList&lt;&gt;();
	 * </pre>
	 *
	 * Hence, the types to put between the brackets are tried to be determined.
	 * Inference will be done by simple matching of an encountered
	 * {@link TypeVariable} in the source to the corresponding type in the
	 * parameters of the destination. If an {@link TypeVariable} is encountered
	 * more than once, the corresponding type in the destination needs to
	 * perfectly match. Else, false will be returned.</br>
	 * </br>
	 * Examples:
	 * <ul>
	 * If we have a class:
	 * <li>
	 *
	 * <pre>
	 * class NumberSupplier&lt;M extends Number&gt; implements Supplier&lt;M&gt;
	 * </li>
	 * </ul>
	 * <ul>
	 * The following check will return true:
	 * <li>
	 *
	 * <pre>
	 * checkGenericAssignability(NumberSupplier.class, new
	 * Nil&lt;Supplier&lt;Double&gt;&gt;() {}.getType())</li>
	 * </ul>
	 * </ul>
	 * <ul>
	 * Which will check if the following assignment would be legal:
	 * <li>
	 *
	 * <pre>
	 * Supplier&lt;Double&gt; list = new NumberSupplier&lt;&gt;()</li>
	 * </ul>
	 * </ul>
	 * <ul>
	 * Here, the parameter {@code <M extends Number>} can be inferred to be of
	 * type {@code Double} from the type {@code Supplier<Double>}
	 * </ul>
	 * <ul>
	 * Consequently the following will return false:
	 * <li>
	 *
	 * <pre>
	 * checkGenericAssignability(NumberSupplier.class, new
	 * Nil&lt;Supplier&lt;String&gt;&gt;() {}.getType())</li>
	 * </ul>
	 * <ul>
	 * {@code <M extends Number>} can't be inferred, as type {@code String} is not
	 * within the bounds of {@code M}.
	 * </ul>
	 * <ul>
	 * Furthermore, the following will return false for:
	 * {@code class NumberFunc<M extends Number> implements Function<M, M>}:
	 * <li>
	 *
	 * <pre>
	 * checkGenericAssignability(NumberSupplier.class, new
	 * Nil&lt;Function&lt;Double, Integer&gt;&gt;() {}.getType())</li>
	 * </ul>
	 * <ul>
	 * {@code <M extends Number>} can't be inferred, as types {@code Double} and
	 * {@code Integer} are ambiguous for {@code M}.
	 * </ul>
	 *
	 * @param src the type for which assignment should be checked from
	 * @param dest the parameterized type for which assignment should be checked
	 *          to
	 * @param typeVarAssigns the map of {@link TypeVariable}s to {@link Type}s to
	 *          populate with what would occur in this scenario; must be empty or
	 *          null
	 * @param safeAssignability used to determine if we want to check if the
	 *          src->dest assignment would be safely assignable even though it
	 *          would cause a compiler error if we explicitly tried to do this
	 *          (useful pretty much only for Op matching)
	 * @return whether and assignment of source to destination would be a legal
	 *         java statement
	 */
	public static boolean checkGenericAssignability(Type src,
		ParameterizedType dest, Map<TypeVariable<?>, Type> typeVarAssigns,
		boolean safeAssignability)
	{
		if (typeVarAssigns == null) {
			typeVarAssigns = new HashMap<>();
		}
		else if (!typeVarAssigns.isEmpty()) {
			throw new IllegalArgumentException(
				"Expected empty typeVarAssigns but contained " + typeVarAssigns.size() +
					" entries");
		}

		// fail fast when raw types are not assignable
		if (!Types.isAssignable(Types.raw(src), Types.raw(dest))) return false;

		// when raw types are assignable, check the type variables of src and dest
		Type[] srcTypes = typeParamsAgainstClass(src, Types.raw(dest));
		Type[] destTypes = dest.getActualTypeArguments();

		// if there are no type parameters in src (w.r.t. dest), do a basic
		// assignability check.
		if (srcTypes.length == 0) return Types.isAssignable(src, dest);
		// if there are type parameters, do a more complicated assignability check.
		boolean result = checkGenericAssignability(srcTypes, destTypes, src, dest,
			typeVarAssigns, safeAssignability);
		return result;
	}

	/**
	 * Finds the levels of casting between {@code origin} and
	 * {@code dest}. Returns 0 if dest and origin are the same. Returns -1 if
	 * dest is not assignable from origin.
	 */
	public static int findCastLevels(final Class<?> dest, final Class<?> origin) {
		if (dest.equals(origin)) return 0;

		int level = 1;
		Class<?> currType = origin;
		// BFS if dest is an interface
		if (dest.isInterface()) {
			final HashSet<String> seen = new HashSet<>();
			final ArrayList<Type> currIfaces = new ArrayList<>(Arrays.asList(currType
				.getGenericInterfaces()));
			do {
				final ArrayList<Type> nextIfaces = new ArrayList<>();
				for (final Type iface : currIfaces) {
					if (seen.contains(iface.getTypeName())) continue;

					final Class<?> cls = getClass(iface);
					if (cls.equals(dest)) return level;
					seen.add(iface.getTypeName());
					nextIfaces.addAll(Arrays.asList(cls.getGenericInterfaces()));
				}
				currIfaces.clear();
				currIfaces.addAll(nextIfaces);
				if (currType.getSuperclass() != null) {
					currType = currType.getSuperclass();
					currIfaces.addAll(Arrays.asList(currType.getGenericInterfaces()));
				}
				level++;
			}
			while (!currIfaces.isEmpty() || currType.getSuperclass() != null);
		}
		// otherwise dest is a class, so search the list of ancestors
		else {
			while (currType.getSuperclass() != null) {
				currType = currType.getSuperclass();
				if (currType.equals(dest)) return level;
				level++;
			}
		}
		return -1;
	}

	/**
	 * Gets the "useful" class information carries on the given object, which
	 * depends on the actual type of the object.
	 */
	public static Class<?> getClass(final Object obj) {
		if (obj == null) return null;
		if (obj instanceof Nil) return getClass(((Nil<?>) obj).getType());
		if (obj instanceof Class) return (Class<?>) obj;
		if (obj instanceof ParameterizedType)
			return (Class<?>) ((ParameterizedType) obj).getRawType();
		return obj.getClass();
	}

	/**
	 * Finds the type parameters of the most specific super type of the specified
	 * subType whose erasure is the specified superErasure. Hence, will return the
	 * type parameters of superErasure possibly narrowed down by subType. If
	 * superErasure is not raw or not a super type of subType, an empty array will
	 * be returned.
	 *
	 * @param subType the type to narrow down type parameters
	 * @param superErasure the erasure of an super type of subType to get the
	 *          parameters from
	 * @return type parameters of superErasure possibly narrowed down by subType,
	 *         or empty type array if no exists or superErasure is not a super
	 *         type of subtype
	 */
	public static Type[] getParams(Class<?> subType, Class<?> superErasure) {
		Type pt = Types.parameterizeRaw(subType);
		Type superType = Types.getExactSuperType(pt, superErasure);
		if (superType != null && superType instanceof ParameterizedType) {
			return ((ParameterizedType) superType).getActualTypeArguments();
		}
		return new Type[0];
	}

	/**
	 * Tries to infer type vars contained in types from corresponding types from
	 * inferFrom, putting them into the specified map. <b>When a
	 * {@link TypeInferenceException} is thrown, the caller should assume that
	 * some of the mappings within {@code typeMappings} are incorrect.</b>
	 *
	 * @param types - the types containing {@link TypeVariable}s
	 * @param inferFroms - the types used to infer the {@link TypeVariable}s
	 *          within {@code types}
	 * @param typeVarAssigns - the mapping of {@link TypeVariable}s to
	 *          {@link Type}s
	 */
	public static void inferTypeVariables(Type[] types, Type[] inferFroms,
		Map<TypeVariable<?>, Type> typeVarAssigns)
	{
		Map<TypeVariable<?>, TypeMapping> typeMappings = new HashMap<>();
		try {
			inferTypeVariables(types, inferFroms, typeMappings, true);
			typeVarAssigns.putAll(new TypeVarAssigns(typeMappings));
		}
		catch (TypeInferenceException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * We know that the special types for the Op candidate and what we asked for
	 * are the same (i.e. that we are trying to determine if one Function can be
	 * assigned to another Function). There are some situations (that are
	 * particularly common when using ops.run()) where the Function SHOULD NOT
	 * NORMALLY MATCH UP but WE KNOW IT WILL BE SAFE TO ASSIGN. This method
	 * attempts to tease those situations out as a last resort.
	 * 
	 * @param destTypes - the array of Parameterized types of the OpInfo we called
	 *          the matcher on (in the case of ops.run(), it is a Type array of
	 *          the types of the args we passed through.)
	 * @param typeVarAssigns - a Map of all of the Type Variables already
	 *          determined.
	 * @param dest - the speical type of the Op that we want to find a match for
	 *          (determined by the user / ops.run())
	 * @return boolean - true if we can safely match this Op even though the types
	 *         do not directly match up. False otherwise.
	 */
	public static boolean isSafeAssignable(Type[] destTypes,
		Map<TypeVariable<?>, Type> typeVarAssigns, Type src, Type dest)
	{

		Method[] destMethods = Arrays.stream(Types.raw(dest).getDeclaredMethods())
			.filter(method -> Modifier.isAbstract(method.getModifiers())).toArray(
				Method[]::new);
		if (destMethods.length == 0) {
			throw new IllegalArgumentException(src +
				" does not have an abstract method!");
		}
		Type[] params = Types.getExactParameterTypes(destMethods[0], src);
		Type returnType = Types.getExactReturnType(destMethods[0], src);
		for (int i = 0; i < params.length; i++) {
			if (!Types.isAssignable(destTypes[i], params[i], typeVarAssigns))
				return false;
		}

		// Computers will have void as their return type, meaning that there is no
		// output to check.
		if (returnType == void.class) return true;

		return Types.isAssignable(returnType, destTypes[destTypes.length - 1],
			typeVarAssigns);
	}

	/**
	 * Map type vars in specified type list to types using the specified map. In
	 * doing so, type vars mapping to other type vars will not be followed but
	 * just replaced.
	 *
	 * @param typesToMap
	 * @param typeAssigns
	 * @return a copy of {@code typesToMap} in which the {@link TypeVariable}s
	 *         (that are present in {@code typeAssigns}) are mapped to the
	 *         associated values within the {@code Map}.
	 */
	public static Type[] mapVarToTypes(Type[] typesToMap,
		Map<TypeVariable<?>, Type> typeAssigns)
	{
		return Arrays.stream(typesToMap).map(type -> Types.unrollVariables(
			typeAssigns, type, false)).toArray(Type[]::new);
	}

	/**
	 * Obtains the type parameters of {@link Type} {@code src} <b>with respect
	 * to</b> the {@link Class} {@code dest}. When {@code src} has no type
	 * parameters (or is not a subclass of {@code dest}), an empty array is
	 * returned.
	 *
	 * @param src - the {@code Type} whose type parameters will be returned.
	 * @param superclass - the {@code Class} against which we want the type
	 *          parameters of {@code src}
	 * @return an array of {@code Type}s denoting the type
	 */
	public static Type[] typeParamsAgainstClass(Type src, Class<?> superclass) {
		// only classes and ParameterizedTypes can have type parameters
		if (!(src instanceof Class || src instanceof ParameterizedType))
			return new Type[0];
		Type superSrc = superType(src, superclass);
		if (superSrc == null) return new Type[0];
		if (superSrc instanceof ParameterizedType)
			return ((ParameterizedType) superSrc).getActualTypeArguments();
		return getParams(Types.raw(src), superclass);
	}

	/**
	 * @param srcTypes the Type arguments for the source Type
	 * @param destTypes the Type arguments for the destination Type
	 * @param src the type for which assignment should be checked from
	 * @param dest the parameterized type for which assignment should be checked
	 *          to
	 * @param typeVarAssigns the map of {@link TypeVariable}s to
	 *          {@link TypeMapping}s that would occur in this scenario
	 * @param safeAssignability used to determine if we want to check if the
	 *          src->dest assignment would be safely assignable even though it
	 *          would cause a compiler error if we explicitly tried to do this
	 *          (useful pretty much only for Op matching)
	 * @return whether and assignment of source to destination would be a legal
	 *         java statement
	 */
	private static boolean checkGenericAssignability(Type[] srcTypes,
		Type[] destTypes, Type src, Type dest,
		Map<TypeVariable<?>, Type> typeVarAssigns, boolean safeAssignability)
	{
		// if the number of type arguments does not match, the types can't be
		// assignable
		if (srcTypes.length != destTypes.length) {
			return false;
		}

		try {
			// Try to infer type variables contained in the type arguments of
			// sry
			inferTypeVariables(srcTypes, destTypes, typeVarAssigns);
		}
		catch (IllegalArgumentException e) {
			// types can't be inferred
			// TODO: Consider the situations in which it is okay that the type
			// variables cannot be inferred. For example, if we have a
			// Function<Comparable<T>, Comparable<T>> and we ask for a
			// Function<Double, Object>, it is okay that we cannot infer the T of
			// Comparable<T> from Object since a Comparable<T> is an Object for any T.
			// It would be nice if we could just return false any time we catch a
			// TypeInferenceException, but until we sort this out, we cannot do so.
			return safeAssignability && isSafeAssignable(destTypes, typeVarAssigns,
				src, dest);
		}

		// Map TypeVariables in src to Types
		Class<?> matchingRawType = Types.raw(dest);
		Type[] mappedSrcTypes = mapVarToTypes(srcTypes, typeVarAssigns);
		Type inferredSrcType = Types.parameterize(matchingRawType, mappedSrcTypes);

		// Check assignability
		if (Types.isAssignable(inferredSrcType, dest, typeVarAssigns)) return true;

		return safeAssignability && isSafeAssignable(destTypes, typeVarAssigns, src,
			dest);
	}

	/**
	 * Current java language specifications allow either:
	 * <ul>
	 * <li>one {@code Object} upper bound and one lower bound
	 * <li>one (arbitrary) upper bound and no lower bounds
	 * </ul>
	 * We rely on this fact for the purposes of inferring type variables.
	 *
	 * @param type
	 * @return the <b>singular</b> {@link Type} that bounds this
	 *         {@link TypeVariable}. The returned {@code Type} could be
	 *         <b>either</b> a lower <b>or</b> upper bound (we do not care for the
	 *         sole purpose of type inference).
	 */
	private static Type getInferrableBound(WildcardType type) {
		Type[] lBounds = type.getLowerBounds();
		Type[] uBounds = type.getUpperBounds();
		if (lBounds.length == 1 && uBounds.length == 1 &&
			uBounds[0] == Object.class) return lBounds[0];
		else if (lBounds.length == 0 && uBounds.length == 1) return uBounds[0];
		else throw new IllegalArgumentException(
			"Illegal WildcardType: Current Java Language Specification does not allow " +
				type + " to simultaneously have upper bounds " + uBounds +
				" and lower bounds " + lBounds);
	}

	private static void inferTypeVariables(Class<?> type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		if (inferFrom instanceof TypeVariable<?>) {
			TypeVarAssigns typeVarAssigns = new TypeVarAssigns(typeMappings);
			// If current type var is absent put it to the map. Otherwise,
			// we already encountered that var.
			// Hence, we require them to be exactly the same.
			if (Types.isAssignable(type, inferFrom, typeVarAssigns)) {
				Type current = typeVarAssigns.putIfAbsent((TypeVariable<?>) inferFrom,
					type);
				if (current != null) {
					if (current instanceof Any) {
						typeVarAssigns.put((TypeVariable<?>) inferFrom, type);
					}
					else if (!Objects.equal(type, current)) {
						throw new TypeInferenceException();
					}
				}
			}
		}
	}

	private static void inferTypeVariables(GenericArrayType type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		if (inferFrom instanceof Class<?> && ((Class<?>) inferFrom).isArray()) {
			Type componentType = type.getGenericComponentType();
			Type componentInferFrom = ((Class<?>) inferFrom).getComponentType();
			inferTypeVariables(componentType, componentInferFrom, typeMappings);
		}
		else if (inferFrom instanceof WildcardType) {
			Type inferrableBound = getInferrableBound((WildcardType) inferFrom);
			inferTypeVariables(type, inferrableBound, typeMappings);
		}
		else throw new TypeInferenceException(inferFrom +
			" cannot be implicitly cast to " + type +
			", thus it is impossible to infer type variables for " + inferFrom);
	}

	private static void inferTypeVariables(ParameterizedType type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		if (inferFrom instanceof WildcardType) {
			inferFrom = getInferrableBound((WildcardType) inferFrom);
		}
		if (inferFrom instanceof Any) {
			Any any = (Any) inferFrom;
			mapTypeVarsToAny(type, any, typeMappings);
			return;
		}
		// Finding the supertype here is really important. Suppose that we are
		// inferring from a StrangeThing<Long> extends Thing<Double> and our
		// Op requires a Thing<T>. We need to ensure that T gets
		// resolved to a Double and NOT a Long.
		Type superInferFrom = Types.getExactSuperType(inferFrom, Types.raw(type));
		if (superInferFrom instanceof ParameterizedType) {
			ParameterizedType paramInferFrom = (ParameterizedType) superInferFrom;
			inferTypeVariables(type.getActualTypeArguments(), paramInferFrom
				.getActualTypeArguments(), typeMappings, false);
		}
		else if (superInferFrom instanceof Class) {
			TypeVarAssigns typeVarAssigns = new TypeVarAssigns(typeMappings);
			Type mappedType = Types.mapVarToTypes(type, typeVarAssigns);
			// Use isAssignable to attempt to infer the type variables present in type
			if (!Types.isAssignable(superInferFrom, mappedType, typeVarAssigns)) {
				throw new TypeInferenceException(inferFrom +
					" cannot be implicitly cast to " + mappedType +
					", thus it is impossible to infer type variables for " + inferFrom);
			}
			// for all remaining unmapped type vars, map to Any
			mapTypeVarsToAny(type, typeMappings);
		}
		// -- edge cases -> do our best -- //
		else if (superInferFrom == null) {
			// edge case 1: if inferFrom is an Object, superInferFrom will be null
			// when type is some interface.
			if (Object.class.equals(inferFrom)) {
				mapTypeVarsToAny(type, typeMappings);
				return;
			}
			// edge case 2: if inferFrom is a superType of type, we can get (some of)
			// the types of type by finding the exact superType of type w.r.t.
			// inferFrom.
			Type superTypeOfType = Types.getExactSuperType(type, Types.raw(
				inferFrom));
			if (superTypeOfType == null) {
				throw new TypeInferenceException(inferFrom +
					" cannot be implicitly cast to " + type +
					", thus it is impossible to infer type variables for " + inferFrom);
			}
			inferTypeVariables(superTypeOfType, inferFrom, typeMappings, false);
			mapTypeVarsToAny(type, typeMappings);
		}
		// TODO: elaborate
		else throw new IllegalStateException(superInferFrom +
			" is the supertype of " + inferFrom + " with respect to " + type +
			", however this cannot be (since " + type +
			" is a ParamterizedType)! (Only a ParameterizedType, Class, or null " +
			"can be returned from Types.getExactSuperType when it is called with a ParameterizedType!)");
	}

	private static void inferTypeVariables(Type type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings, boolean malleable)
	{
		if (type instanceof TypeVariable) {
			inferTypeVariables((TypeVariable<?>) type, inferFrom, typeMappings,
				malleable);
		}
		else if (type instanceof ParameterizedType) {
			inferTypeVariables((ParameterizedType) type, inferFrom, typeMappings);
		}
		else if (type instanceof WildcardType) {
			inferTypeVariables((WildcardType) type, inferFrom, typeMappings);
		}
		else if (type instanceof GenericArrayType) {
			inferTypeVariables((GenericArrayType) type, inferFrom, typeMappings);
		}
		else if (type instanceof Class) {
			inferTypeVariables((Class<?>) type, inferFrom, typeMappings);
		}

	}

	private static void inferTypeVariables(Type[] types, Type[] inferFroms,
		Map<TypeVariable<?>, TypeMapping> typeMappings, boolean malleable)
	{
		// Ensure that the user has not passed a null map
		if (typeMappings == null) throw new IllegalArgumentException(
			"Type Variable map is null, cannot store mappings of TypeVariables to Types!");

		if (types.length != inferFroms.length) throw new TypeInferenceException(
			"Could not infer type variables: Type arrays must be of the same size");

		for (int i = 0; i < types.length; i++) {
			inferTypeVariables(types[i], inferFroms[i], typeMappings, malleable);
		}
		// Check if the inferred types satisfy their bounds
		// TODO: can we do this in an efficient manner?
		TypeVarAssigns typeVarAssigns = new TypeVarAssigns(typeMappings);
		if (!Types.typesSatisfyVariables(typeVarAssigns)) {
			throw new TypeInferenceException();
		}
	}

	private static void inferTypeVariables(TypeVariable<?> type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings, boolean malleable)
	{
		TypeMapping typeData = typeMappings.get(type);
		// If current is not null then we have already encountered that
		// variable. If so, we require them to be exactly the same, and throw a
		// TypeInferenceException if they are not.
		if (typeData != null) {
			typeData.refine(inferFrom, malleable);
		}
		else {
			resolveTypeInMap(type, inferFrom, typeMappings, malleable);
			// Bounds could also contain type vars, hence possibly go into
			// recursion
			for (Type bound : type.getBounds()) {
				if (bound instanceof TypeVariable && typeMappings.get(bound) != null) {
					// If the bound of the current var (let's call it A) to
					// infer is also a var (let's call it B):
					// If we already encountered B, we check if the current
					// type to infer from is assignable to
					// the already inferred type for B. In this case we do
					// not require equality as one var is
					// bounded by another and it is not the same. E.g.
					// assume we want to infer the types of vars:
					// - - - A extends Number, B extends A
					// From types:
					// - - - Number, Double
					// First A is bound to Number, next B to Double. Then we
					// check the bounds for B. We encounter A,
					// for which we already inferred Number. Hence, it
					// suffices to check whether Double can be assigned
					// to Number, it does not have to be equal as it is just
					// a transitive bound for B.
					Type typeAssignForBound = typeMappings.get(bound).getType();
					if (!Types.isAssignable(inferFrom, typeAssignForBound)) {
						throw new TypeInferenceException();
					}
				}
				else {
					// Else go into recursion as we encountered a new var.
					inferTypeVariables(bound, inferFrom, typeMappings);
				}
			}

		}
	}

	private static void inferTypeVariables(WildcardType type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		Type inferrableBound = getInferrableBound(type);
		if (inferFrom instanceof WildcardType) {
			// NB if both type and inferFrom are Wildcards, it doesn't really matter
			// (for the purpose of Type inference) whether those Wildcards have a
			// defined upper or lower bound. It is only important that we compare
			// those defined bounds, even if one is an upper bound and the other is a
			// lower bound. If the Wildcards are not assignable (which is (always?)
			// the case when one bound is an upper bound and the other is a lower
			// bound), it is still possible to infer the type variables; despite doing
			// so, checkGenericAssignability will return false.
			inferFrom = getInferrableBound((WildcardType) inferFrom);
		}
		if (inferrableBound instanceof TypeVariable<?>) {
			resolveTypeInMap((TypeVariable<?>) inferrableBound, inferFrom,
				typeMappings, true);
		}
		else if (inferrableBound instanceof ParameterizedType) {
			ParameterizedType parameterizedUpperBound =
				(ParameterizedType) inferrableBound;
			inferTypeVariables(parameterizedUpperBound, inferFrom, typeMappings,
				true);
		}
		// TODO: consider checking inferrableBounds instanceof Class
	}

	private static void mapTypeVarsToAny(Type type, Any any,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		if (!Types.containsTypeVars(type)) return;

		if (type instanceof TypeVariable) {
			if (typeMappings.containsKey(type)) return;
			TypeVariable<?> typeVar = (TypeVariable<?>) type;
			typeMappings.put(typeVar, suitableTypeMapping(typeVar, any, true));
		}
		else if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type[] typeParams = pType.getActualTypeArguments();
			for (Type typeParam : typeParams) {
				mapTypeVarsToAny(typeParam, typeMappings);
			}
		}
		else if (type instanceof WildcardType) {
			WildcardType wildcard = (WildcardType) type;
			for (Type lowerBound : wildcard.getLowerBounds())
				mapTypeVarsToAny(lowerBound, typeMappings);
			for (Type upperBound : wildcard.getUpperBounds())
				mapTypeVarsToAny(upperBound, typeMappings);
		}
		else if (type instanceof Class) {
			Class<?> clazz = (Class<?>) type;
			for (Type typeParam : clazz.getTypeParameters())
				mapTypeVarsToAny(typeParam, typeMappings);
		}
	}

	private static void mapTypeVarsToAny(Type type,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		mapTypeVarsToAny(type, new Any(), typeMappings);
	}

	private static void resolveTypeInMap(TypeVariable<?> typeVar, Type newType,
		Map<TypeVariable<?>, TypeMapping> typeMappings, boolean malleability)
	{
		if (typeMappings.containsKey(typeVar)) {
			typeMappings.get(typeVar).refine(newType, malleability);
		}
		else {
			typeMappings.put(typeVar, suitableTypeMapping(typeVar, newType,
				malleability));
		}
	}

	private static TypeMapping suitableTypeMapping(TypeVariable<?> typeVar,
		Type newType, boolean malleability)
	{
		if (newType instanceof WildcardType) {
			return new WildcardTypeMapping(typeVar, (WildcardType) newType,
				malleability);
		}
		return new TypeMapping(typeVar, newType, malleability);
	}

	/**
	 * This method is designed to handle edge cases when calling
	 * {@link Types#getExactSuperType(Type, Class)}. When
	 * {@code getExactSuperType} returns an error, this usually implies some funny
	 * business going on with the {@link Type} that was passed to it. We are not
	 * interested in supporting this business, since it usually results from poor
	 * practice in Class construction. TODO: determine some way of conveying to
	 * the user that GenTyRef doesn't like their inputs.
	 *
	 * @param src
	 * @param superClass
	 * @return - the supertype of {@code src} with rawtype {@code superClass}, or
	 *         {@code null} if no such supertype exists.
	 */
	private static Type superType(Type src, Class<?> superClass) {
		try {
			return Types.getExactSuperType(src, superClass);
		}
		catch (AssertionError e) {
			// can be thrown when
			return null;
		}
	}

	/**
	 * Tries to infer type vars contained in types from corresponding types from
	 * inferFrom, putting them into the specified map. <b>When a
	 * {@link TypeInferenceException} is thrown, the caller should assume that
	 * some of the mappings within {@code typeMappings} are incorrect.</b>
	 *
	 * @param type
	 * @param inferFrom
	 * @param typeMappings
	 */
	static void inferTypeVariables(Type type, Type inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		inferTypeVariables(type, inferFrom, typeMappings, true);
	}

	/**
	 * Tries to infer type vars contained in types from corresponding types from
	 * inferFrom, putting them into the specified map. <b>When a
	 * {@link TypeInferenceException} is thrown, the caller should assume that
	 * some of the mappings within {@code typeMappings} are incorrect.</b>
	 *
	 * @param type
	 * @param inferFrom
	 * @param typeMappings
	 */
	static void inferTypeVariablesWithTypeMappings(Type type[], Type[] inferFrom,
		Map<TypeVariable<?>, TypeMapping> typeMappings)
	{
		inferTypeVariables(type, inferFrom, typeMappings, true);
	}

}
