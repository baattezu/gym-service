package org.saltaonelove.dao.utils;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Deprecated
@Component
public class IdGenerator {

    private final Map<String, AtomicLong> idMap = new ConcurrentHashMap<>();

    public void initialize(String namespace, long maxId) {
        idMap.compute(namespace, (key, existingValue) -> {
            if (existingValue == null || maxId > existingValue.get()) {
                return new AtomicLong(maxId + 1);
            }
            return existingValue;
        });
    }

    public Long nextId(String namespace) {
        return idMap.computeIfAbsent(namespace, k -> new AtomicLong(1)).getAndIncrement();
    }
}