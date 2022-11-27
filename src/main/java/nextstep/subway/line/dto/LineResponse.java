package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static LineResponse of(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private LineResponse() {}

    private LineResponse(
            Long id,
            String name,
            String color,
            List<Station> stations,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
