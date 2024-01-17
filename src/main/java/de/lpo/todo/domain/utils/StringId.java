package de.lpo.todo.domain.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Implementation of an ID having a string value.
 * The conversion between {@link #value()} and {@link #hash()} is complexer than for numbers.
 */
public class StringId extends Id<String> {

    @NotNull
    public static Id<String> ofValue(@NotNull String id) {
        return new StringId(id, null);
    }

    @Nullable
    public static Id<String> ofHash(@Nullable String hash) {
        if (StringUtils.isBlank(hash)) {
            return null;
        }
        return new StringId(null, hash);
    }

    protected StringId(String value, String hash) {
        super(value, hash);
    }

    @Override
    protected String convertValueToHash(String value) {
        // Convert String into UTF-8 bytes
        final var valueBytes = value.getBytes(StandardCharsets.UTF_8);

        // Convert these bytes to longs
        final var bytesAsLongs = new ArrayList<Long>(valueBytes.length);
        for (final var valueByte : valueBytes) {
            bytesAsLongs.add((long) valueByte);
        }

        // Encode all longs into one hash
        return SQIDS.encode(bytesAsLongs);
    }

    @Override
    protected String convertHashToValue(String hash) {
        // Decode the hash into multiple longs (each representing an UTF-8 character)
        final var bytesAsLongs = SQIDS.decode(hash);

        // Convert the List<Long> into byte[]
        final var valueBytes = new byte[bytesAsLongs.size()];
        for (int i = 0; i < bytesAsLongs.size(); i++) {
            valueBytes[i] = bytesAsLongs.get(i).byteValue();
        }

        // Recover the original String from the UTF-8 encoded byte array
        return new String(valueBytes, StandardCharsets.UTF_8);
    }
}
