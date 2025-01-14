package org.scijava.discovery.therapi;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.scijava.discovery.Discoverer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TherapiDiscovererTest {

	private Discoverer discoverer() {
		return new TaggedElementDiscoverer("test");
	}

	@Test
	public void discoverClass() {
		List<TaggedElement> elements = discoverer().discover(TaggedElement.class);
		assertTrue(elements.stream().anyMatch(e -> e.discovery() == ClassTest.class));
	}

	@Test
	public void discoverField() throws SecurityException {
		List<TaggedElement> elements = discoverer().discover(TaggedElement.class);
		assertTrue(elements.stream().anyMatch(d -> {
			try {
				AnnotatedElement actual = d.discovery();
				AnnotatedElement expected = this.getClass().getDeclaredField("fieldTest");
				return expected.equals(actual);
			} catch (NoSuchFieldException ex) {
				return false;
			}
		}));
	}

	@Test
	public void discoverMethod() throws SecurityException {
		List<TaggedElement> elements = discoverer().discover(TaggedElement.class);
		assertTrue(elements.stream().anyMatch(d -> {
			try {
				AnnotatedElement actual = d.discovery();
				AnnotatedElement expected = this.getClass().getDeclaredMethod("methodTest");
				return expected.equals(actual);
			} catch (NoSuchMethodException ex) {
				return false;
			}
		}));
	}

	/**
	 * @implNote test
	 */
	public void methodTest() {

	}

	/**
	 * @implNote test
	 */
	public final Function<Integer, Integer> fieldTest = (in) -> in + 1;
}

/**
 * @implNote test
 * @author gselz
 *
 */
class ClassTest {

}
