package org.thane;

import java.util.Map;

public class MapBuilder<K, V> {
    private Map<K, V> map;

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public Map<K, V> asMap() {
        return map;
    }

    public MapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> remove(K key) {
        map.remove(key);
        return this;
    }

    public MapBuilder<K, V> putIfAbsent(K key, V value) {
        map.putIfAbsent(key, value);
        return this;
    }

    public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> map) {
        this.map.putAll(map);
        return this;
    }
}
