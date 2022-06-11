package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public class StationPathDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static StationPathDTO of(Station station){
        return new StationPathDTO(station.getId(), station.getName(), station.getCreatedDate());
    }

    protected StationPathDTO(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
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
