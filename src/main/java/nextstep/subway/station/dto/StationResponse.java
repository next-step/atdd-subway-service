package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationName;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(),
                                   Optional.ofNullable(station.getName()).map(StationName::getValue).orElse(""),
                                   station.getCreatedDate(),
                                   station.getModifiedDate());
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
