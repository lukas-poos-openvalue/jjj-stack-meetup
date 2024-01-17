package de.lpo.todo.domain.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Implementation of an ID having an integer value.
 */
public class IntId extends Id<Integer> {

    @NotNull
    public static Id<Integer> ofValue(int id) {
        return new IntId(id, null);
    }

    @Nullable
    public static Id<Integer> ofHash(@Nullable String hash) {
        if (StringUtils.isBlank(hash)) {
            return null;
        }
        return new IntId(null, hash);
    }

    protected IntId(Integer value, String hash) {
        super(value, hash);
    }

    @Override
    protected String convertValueToHash(Integer value) {
        return SQIDS.encode(List.of((long) value));
    }

    @Override
    protected Integer convertHashToValue(String hash) {
        return SQIDS.decode(hash).getFirst().intValue();
    }
}
