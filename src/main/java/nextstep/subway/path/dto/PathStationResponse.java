package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class PathStationResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public PathStationResponse() {}

    public PathStationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
        this.createdAt = station.getCreatedDate();
    }

    public static PathStationResponse of(Station station) {
        return new PathStationResponse(station);
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
