package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PathStationResponse))
            return false;
        PathStationResponse that = (PathStationResponse)o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate);
    }
}
