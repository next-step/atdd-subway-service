package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class PathStation {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    public PathStation(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static PathStation of(Station station) {
        return new PathStation(station.getId(), station.getName(), station.getCreatedDate());
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
}
