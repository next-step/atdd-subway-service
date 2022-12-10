package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;

public class StationPathResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;

    protected StationPathResponse(){

    }
    private StationPathResponse(Long id, String name, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }

    public static StationPathResponse from(Station station) {
        return new StationPathResponse(station.getId(),station.getName(),station.getCreatedDate());
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
