package lc.mine.core.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public final class Config {

    private Map<String, Object> map;

    public Config(Map<String, Object> map) {
        this.map = map;
    }

    public static Config of(final Yaml yaml, final File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {   
            final Map<String, Object> values = yaml.loadAs(reader, Map.class);
            return new Config(values);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> map() {
        return this.map;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String key) {
        final Object object = map.get(key);
        if (object == null) {
            return null;
        }
        try {
            return (T)object;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T getOrDefault(final String key, final T defaultValue) {
        final T value = get(key);
        return (value == null) ? defaultValue : value;
    }

    public String string(final String key) {
        final Object object = map.get(key);
        return (object instanceof String) ? ((String)object) : null;
    }

    public List<?> list(final String key) {
        final Object object = map.get(key);
        return (object instanceof List<?>) ? ((List<?>)object) : null;    
    }

    public int getInt(final String key) {
        final Object object = map.get(key);
        return (object instanceof Number) ? ((Number)object).intValue() : 0;
    }

    public boolean getBoolean(final String key) {
        final Object object = map.get(key);
        return (object instanceof Boolean) ? ((Boolean)object) : false;
    }

    public double getDouble(final String key) {
        final Object object = map.get(key);
        return (object instanceof Number) ? ((Number)object).doubleValue() : 0;    
    }

    @SuppressWarnings("unchecked")
    public Config getSection(final String key) {
        final Object object = map.get(key);
        return (object instanceof Map) ? new Config((Map<String, Object>)object) : null;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}