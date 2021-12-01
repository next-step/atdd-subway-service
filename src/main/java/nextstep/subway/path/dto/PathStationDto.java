package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import nextstep.subway.common.SubwayUtil;
import nextstep.subway.station.domain.Station;

public class PathStationDto {
    private Long id;
    private String name;
    private String createdAt;

    public PathStationDto() {
    }

    public PathStationDto(Long id, String name, String createdAt) {
        this.id = id;
        this.name = name;

        this.createdAt = createdAt;
    }

    public static PathStationDto of(Station station) {
        return new PathStationDto(station.getId(), station.getName(), SubwayUtil.convertLocalDateTime(station.getCreatedDate()));
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PathStationDto)) {
            return false;
        }
        PathStationDto pathStationDto = (PathStationDto) o;
        return Objects.equals(id, pathStationDto.id) && Objects.equals(name, pathStationDto.name) && Objects.equals(createdAt, pathStationDto.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdAt);
    }
}
