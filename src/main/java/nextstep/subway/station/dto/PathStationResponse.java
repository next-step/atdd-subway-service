package nextstep.subway.station.dto;

import java.time.LocalDateTime;

import nextstep.subway.station.domain.Station;

public class PathStationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;

    public static PathStationResponse of(Station station) {
        return new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate());
    }

    protected PathStationResponse() {
    }

    public PathStationResponse(Long id, String name, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
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
