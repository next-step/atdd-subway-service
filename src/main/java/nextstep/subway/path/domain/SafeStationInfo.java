package nextstep.subway.path.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class SafeStationInfo {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    public SafeStationInfo(final Long id, final String name, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SafeStationInfo that = (SafeStationInfo) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdAt);
    }
}
