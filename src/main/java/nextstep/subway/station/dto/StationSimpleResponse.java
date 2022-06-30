package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public class StationSimpleResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public StationSimpleResponse() {

    }

    private StationSimpleResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static StationSimpleResponse from(Station station) {
        return new StationSimpleResponse(
            station.getId(),
            station.getName(),
            station.getCreatedDate()
        );
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
