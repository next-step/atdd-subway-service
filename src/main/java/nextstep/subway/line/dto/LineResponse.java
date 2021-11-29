package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineColor;
import nextstep.subway.line.domain.LineName;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int fare;

    public LineResponse() {}

    public LineResponse(Long id,
                        String name,
                        String color,
                        int fare,
                        List<StationResponse> stations,
                        LocalDateTime createdDate,
                        LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.fare = fare;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(),
                                Optional.ofNullable(line.getName()).map(LineName::getValue).orElse(""),
                                Optional.ofNullable(line.getColor()).map(LineColor::getValue).orElse(""),
                                Optional.ofNullable(line.getFare()).map(Fare::getValue).orElse(0),
                                stations,
                                line.getCreatedDate(),
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
}
