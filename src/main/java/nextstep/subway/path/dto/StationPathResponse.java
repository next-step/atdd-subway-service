package nextstep.subway.path.dto;

import java.time.LocalDateTime;

public class StationPathResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;

    public StationPathResponse(Long id, String name, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
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
