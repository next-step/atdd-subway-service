package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int fare;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate, int fare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.fare = fare;
    }

    public static LineResponse of(Line line) {
        if(line.assembleStations().isEmpty()) {
            return new LineResponse(line.getId(), line.getName(), line.getColor(), new ArrayList(), line.getCreatedDate(), line.getModifiedDate(), line.getFare());
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor(), getStations(line), line.getCreatedDate(), line.getModifiedDate(), line.getFare());
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

    public int getFare() {
        return fare;
    }
    private static List<StationResponse> getStations(Line line) {
        return line.assembleStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
