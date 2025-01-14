package org.scijava.ops.engine.yaml;

import java.net.URI;
import java.util.Map;

import org.scijava.ops.api.Hints;
import org.scijava.ops.api.OpInfo;
import org.scijava.ops.api.features.YAMLOpInfoCreator;

/**
 * An abstract base class for parsing the YAML into values common to {@link OpInfo}s.
 *
 * @author Gabriel Selzer
 */
public abstract class AbstractYAMLOpInfoCreator implements YAMLOpInfoCreator {

    @Override
    public OpInfo create(final URI identifier, final Map<String, Object> yaml) {
        // Parse path - start after the leading slash
        final String path = identifier.getPath().substring(1);
        // Parse source
        final String srcString = path.substring(0, path.indexOf('/'));
        // Parse version
        final String version = path.substring(path.indexOf('/') + 1);
        // Parse names
        final String[] names;
        if (yaml.containsKey("name")) {
            names = new String[]{(String) yaml.get("name")};
        } else {
            String namesString = (String) yaml.get("names");
            names = namesString.split("\\s*,\\s*");
        }
        // Parse priority
        double priority = 0.0;
        if (yaml.containsKey("priority")) {
            Object p = yaml.get("priority");
            if (p instanceof Number) priority = ((Number) p).doubleValue();
            else if (p instanceof String) {
                priority = Double.parseDouble((String) p);
            } else {
                throw new IllegalArgumentException("Op priority not parsable");
            }
        }
        // Create the OpInfo
        try {
            return create(srcString, names, priority, null, version, yaml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    abstract OpInfo create(final String identifier, final String[] names, final double priority, final Hints hints, final String version, Map<String, Object> yaml) throws Exception;
}
