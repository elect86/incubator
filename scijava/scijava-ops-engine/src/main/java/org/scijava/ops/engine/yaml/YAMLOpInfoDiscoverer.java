
package org.scijava.ops.engine.yaml;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import org.scijava.discovery.Discoverer;
import org.scijava.ops.api.OpInfo;
import org.scijava.ops.api.features.YAMLOpInfoCreator;
import org.yaml.snakeyaml.Yaml;

/**
 * A {@link Discoverer} implementation that can discover {@link OpInfo}s from
 * YAML.
 *
 * @author Gabriel Selzer
 */
public class YAMLOpInfoDiscoverer implements Discoverer {

	private final Yaml yaml = new Yaml();

	private final List<YAMLOpInfoCreator> creators = Discoverer.using(
		ServiceLoader::load).discover(YAMLOpInfoCreator.class);

	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> discover(Class<U> c) {
		// We only discover OpInfos
		if (!c.equals(OpInfo.class)) return Collections.emptyList();
		// Load all YAML files
		Enumeration<URL> opFiles = getOpYAML();
		// Parse each YAML file
		List<OpInfo> opInfos = new ArrayList<>();
		opFiles.asIterator().forEachRemaining(opFile -> {
			try {
				parse(opInfos, opFiles.nextElement());
			}
			catch (IOException e) {
				new IllegalArgumentException( //
					"Could not read Op YAML file " + opFile.toString() + ": ", //
					e) //
						.printStackTrace();
			}
		});
		return (List<U>) opInfos;
	}

	/**
	 * Convenience method to hide IOException
	 * 
	 * @return an {@link Enumeration} of YAML files.
	 */
	private Enumeration<URL> getOpYAML() {
		try {
			return Thread.currentThread() //
				.getContextClassLoader() //
				.getResources("ops.yaml");
		}
		catch (IOException e) {
			throw new RuntimeException("Could not load Op YAML files!", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void parse(List<OpInfo> infos, final URL url)
			throws IOException
	{
		Map<String, Object> yamlData = yaml.load(url.openStream());

		for ( //
		Map<String, Object> op : //
		(List<Map<String, Object>>) yamlData.get("ops")) //
		{
			Map<String, Object> opData = subMap(op, "op");
			String identifier = value(opData, "source");
			try {
				URI uri = new URI(identifier.replaceAll("\\s*", ""));
				Optional<YAMLOpInfoCreator> c = creators.stream() //
					.filter(f -> f.canCreateFrom(uri)) //
					.findFirst();
				if (c.isPresent()) infos.add(c.get().create(uri, opData));
			}
			catch (Exception e) {
				// TODO: Use SciJava Log2's Logger to notify the user.
				// See https://github.com/scijava/scijava/issues/106 for discussion
				// and progress
				System.out.println("Could not add op " + identifier + ":");
				e.printStackTrace();
			}
		}
	}

	private static Map<String, Object> subMap(final Map<String, Object> map,
		String key)
	{
		if (!map.containsKey(key)) {
			throw new IllegalArgumentException("YAML map " + map +
				" does not contain key " + key);
		}
		Object value = map.get(key);
		if (!(value instanceof Map)) {
			throw new IllegalArgumentException("YAML map " + map +
				" has a non-map value for key " + key);
		}
		return (Map<String, Object>) value;
	}

	private static String value(final Map<String, Object> map, String key) {
		if (!map.containsKey(key)) {
			throw new IllegalArgumentException("YAML map " + map +
				" does not contain key " + key);
		}
		Object value = map.get(key);
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("YAML map " + map +
				" has a non-string value for key " + key);
		}
		return (String) value;
	}

}
