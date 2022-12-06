package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class StationPathResponse {

    private long id;
    private String name;
    private LocalDateTime createdAt;

    public StationPathResponse(long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static StationPathResponse from(Station it) {
        return new StationPathResponse(it.getId(), it.getName(), it.getCreatedDate());
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }
}
