package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

public final class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse() {
    }

    private LineResponse(Long id, String name, String color, List<StationResponse> stations,
        LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.id(),
            line.name().toString(),
            line.color().toString(),
            stationResponses(line),
            line.getCreatedDate(),
            line.getModifiedDate());
    }

    public static List<LineResponse> listOf(List<Line> line) {
        return line.stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
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

    private static List<StationResponse> stationResponses(Line line) {
        return line.sortedStations()
            .stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
    }
}
