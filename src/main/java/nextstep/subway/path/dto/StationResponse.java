package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public StationResponse() {
    }

    private StationResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDate());
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
