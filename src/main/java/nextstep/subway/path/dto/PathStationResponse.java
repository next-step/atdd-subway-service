package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public final class PathStationResponse {

    private Long id;
    private String name;
    private LocalDateTime createdDate;

    private PathStationResponse() {
    }

    private PathStationResponse(Long id, String name, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }

    static PathStationResponse from(Station station) {
        return new PathStationResponse(station.id(),
            station.name().toString(),
            station.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
