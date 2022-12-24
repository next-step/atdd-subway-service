package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int cost;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, int cost, List<StationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.cost = cost;
    }

    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getCost(), stations, line.getCreatedDate(), line.getModifiedDate());
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

    public int getCost() {
        return cost;
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
