package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse(Long id, String name, String color, List<StationResponse> stations, LocalDateTime createdDate,
        LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse from(Line line) {
        List<StationResponse> stationResponses = StationResponses
            .from(line.getStations())
            .getResponses();

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses, line.getCreatedDate(),
            line.getModifiedDate());
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
        return Collections.unmodifiableList(stations);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
