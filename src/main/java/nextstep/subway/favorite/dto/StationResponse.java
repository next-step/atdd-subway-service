package nextstep.subway.favorite.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public class StationResponse {

    private final long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public StationResponse(long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(),
                station.getModifiedDate());
    }

    public long getId() {
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
