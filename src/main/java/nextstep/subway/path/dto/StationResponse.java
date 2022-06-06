package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public class StationResponse {
    private final long id;
    private final String name;
    private final LocalDateTime createdAt;

    public StationResponse(long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDate());
    }

    public String getName() {
        return name;
    }
}
