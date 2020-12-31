package nextstep.subway.path.ui.dto;

import nextstep.subway.path.domain.SafeStationInfo;

import java.time.LocalDateTime;
import java.util.Objects;

public class StationInPathResponse {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    public StationInPathResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public StationInPathResponse(SafeStationInfo safeStationInfo) {
        this.id = safeStationInfo.getId();
        this.name = safeStationInfo.getName();
        this.createdAt = safeStationInfo.getCreatedAt();
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
        final StationInPathResponse that = (StationInPathResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdAt);
    }

    @Override
    public String toString() {
        return "StationInPathResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
