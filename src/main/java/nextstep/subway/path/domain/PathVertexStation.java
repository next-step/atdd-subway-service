package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class PathVertexStation {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    public static PathVertexStation of(Station station) {
        return new PathVertexStation(station.getId(), station.getName(), station.getCreatedDate());
    }

    public PathVertexStation() {
    }

    public PathVertexStation(Long id, String name, LocalDateTime createdAt) {
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
}
