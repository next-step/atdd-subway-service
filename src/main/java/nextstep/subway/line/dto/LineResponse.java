package nextstep.subway.line.dto;

import java.util.stream.Collectors;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Name;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int lineFare;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse() {}

    private LineResponse(Long id, Name name, Color color, List<StationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate, Fare lineFare) {
        this.id = id;
        this.name = name.value();
        this.color = color.value();
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.lineFare = lineFare.value();
    }

    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, line.getCreatedDate(), line.getModifiedDate(), line.getLineFare());
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                line.findInOrderStations()
                        .stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList()),
                line.getCreatedDate(),
                line.getModifiedDate(),
                line.getLineFare());
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

    public int getLineFare() {
        return lineFare;
    }
}
