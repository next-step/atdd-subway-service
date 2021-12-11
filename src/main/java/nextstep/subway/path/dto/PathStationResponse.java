package nextstep.subway.path.dto;

import java.time.LocalDateTime;

public class PathStationResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public PathStationResponse() {
    }

    private PathStationResponse(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static PathStationResponse of(Long id, String name, LocalDateTime createdAt) {
        return new PathStationResponse(id, name, createdAt);
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
