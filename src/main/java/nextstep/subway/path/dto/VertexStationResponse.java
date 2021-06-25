package nextstep.subway.path.dto;

import java.time.LocalDateTime;

import nextstep.subway.station.domain.Station;

public class VertexStationResponse {

    private Long id;
    private String name;
    private LocalDateTime createdDate;

    protected VertexStationResponse() {
    }

    private VertexStationResponse(Long id, String name, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }

    public static VertexStationResponse of(Station station) {
        return new VertexStationResponse(station.getId(), station.getName(), station.getCreatedDate());
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
