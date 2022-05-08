package com.example.crescente.data.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This interface allows to manipulate entities with a unique name property in a universal way.
 */
public interface NamedEntity {
    String getName();

    static Set<String> convertNamedEntitiesToNamesSet(
            Collection<? extends NamedEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptySet();
        }

        // LinkedHashSet - to preserve the order
        Set<String> concise = new LinkedHashSet<>(entities.size());
        for (NamedEntity entity : entities) {
            concise.add(entity.getName());
        }
        return concise;
    }

    static List<String> convertNamedEntitiesToNamesList(Collection<? extends NamedEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> concise = new ArrayList<>(entities.size());
        for (NamedEntity entity : entities) {
            concise.add(entity.getName());
        }
        return concise;
    }

    /**
     * Note that if entities is empty an empty map is returned which cannot be modified!
     */
    static <T extends NamedEntity> Map<String, T> convertNamedEntitiesToMap(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, T> map = new LinkedHashMap<>();
        for (T entity : entities) {
            map.put(entity.getName(), entity);
        }
        return map;
    }


    Comparator<NamedEntity> COMPARATOR = Comparator.comparing(NamedEntity::getName);

    Comparator<NamedEntity> REVERSE_COMPARATOR = Collections.reverseOrder(COMPARATOR);
}