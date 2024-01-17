package de.lpo.todo.domain.utils;

import org.jetbrains.annotations.NotNull;
import org.sqids.Sqids;

import java.util.Objects;

/**
 * Base class for an ID.
 * An ID can provide a {@link #hash()}, which is a unique, short representation of the ID's {@link #value()},
 * which can be used externally.
 */
public abstract class Id<T> {
    /**
     * [PoI] Generator for short unique representations of the ID's value.
     */
    protected static final Sqids SQIDS = Sqids.builder()
            .minLength(5)
            .alphabet("k3G7QAe51FCsPW92uEOyq4Bg6Sp8YzVTmnU0liwDdHXLajZrfxNhobJIRcMvKt")
            .build();

    private T value;
    private String hash;

    protected Id(T value, String hash) {
        if (value == null && hash == null) {
            throw new IllegalArgumentException("ID cannot be constructed without value and hash!");
        }
        this.value = value;
        this.hash = hash;
    }

    public @NotNull T value() {
        if (value == null) {
            // Lazily convert hash into the original value
            value = convertHashToValue(hash);
        }
        return value;
    }

    public @NotNull String hash() {
        if (hash == null) {
            // Lazily convert the ID into the hash
            hash = convertValueToHash(value);
        }
        return hash;
    }

    protected abstract String convertValueToHash(T value);

    protected abstract T convertHashToValue(String hash);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id<?> oId)) return false;

        // Use getters here, so that for comparisons, the value and the hash are set
        return Objects.equals(value(), oId.value()) && Objects.equals(hash(), oId.hash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, hash);
    }

    @Override
    public String toString() {
        return "Id{" +
                "value=" + value +
                ", hash='" + hash + '\'' +
                '}';
    }
}
