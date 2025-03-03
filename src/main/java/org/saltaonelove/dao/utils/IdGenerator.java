package org.saltaonelove.dao.utils;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {

    private final Map<String, AtomicLong> idMap = new ConcurrentHashMap<>();

    public Long nextId(String namespace) {
        return idMap.computeIfAbsent(namespace, k -> new AtomicLong(1)).getAndIncrement();
    }
}