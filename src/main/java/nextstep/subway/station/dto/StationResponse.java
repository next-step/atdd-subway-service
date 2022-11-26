package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import nextstep.subway.line.domain.Name;
import nextstep.subway.station.domain.Station;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private StationResponse() {}

    private StationResponse(Long id, Name name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name.value();
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
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
