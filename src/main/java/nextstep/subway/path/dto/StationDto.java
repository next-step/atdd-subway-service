package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import nextstep.subway.station.domain.Station;

public class StationDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    protected StationDto() {
    }

    private StationDto(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public static StationDto from(Station station) {
        return new StationDto(station.getId(), station.getName(), station.getCreatedDate());
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
