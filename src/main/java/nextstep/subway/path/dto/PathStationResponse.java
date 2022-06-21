package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public class PathStationResponse {
    public Long id;

    private String name;

    private LocalDateTime createdAt;

    public static PathStationResponse of(final Station station) {
        return new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate());
    }

    public PathStationResponse() {
    }

    public PathStationResponse(final Long id, final String name, final LocalDateTime createdAt) {
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
