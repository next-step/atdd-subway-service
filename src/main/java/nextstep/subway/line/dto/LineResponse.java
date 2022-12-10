package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Integer additionalFee;
    private List<StationResponse> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, Integer additionalFee, List<StationResponse> stations,
        LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line, List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFee().get(), stations,
            line.getCreatedDate(),
            line.getModifiedDate());
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stations = line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFee().get(), stations,
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

    public Integer getAdditionalFee() {
        return additionalFee;
    }
}
