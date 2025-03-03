package org.saltaonelove.dao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class Storage {
    private final Map<String, Map<Long, Object>> storage = new ConcurrentHashMap<>();


    public <T> void save(String namespace, Long id, T entity) {
        storage.computeIfAbsent(namespace, k -> new ConcurrentHashMap<>()).put(id, entity);
    }

    public <T> T findById(String namespace, Long id) {
        return (T) storage.getOrDefault(namespace, Map.of()).get(id);
    }

    public void delete(String namespace, Long id) {
        Map<Long, Object> map = storage.get(namespace);
        if (map != null) {
            map.remove(id);
        }
    }

    public <T> List<T> findAll(String namespace) {
        return storage.getOrDefault(namespace, Map.of()).values().stream()
                .map(obj -> (T) obj) // Прямое кастование
                .toList();
    }
}