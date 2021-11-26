package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PathStationDto {
    private Long id;
    private String name;
    private String createdAt;

    public PathStationDto() {
    }

    public PathStationDto(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;

        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS"));
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
