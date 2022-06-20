package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public LineResponse(final Long id, final String name, final String color,
                        final List<StationResponse> stations, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static List<LineResponse> ofLines(final List<Line> lines) {
        return Collections.unmodifiableList(lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList()));
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                getAllStations(line.getAllStations()), line.getCreatedDate(), line.getModifiedDate());
    }

    private static List<StationResponse> getAllStations(final List<Station> stations) {
        if (stations.size() == 0) {
            return new ArrayList<>();
        }

        return stations.stream()
                .map(StationResponse::of)
                .sorted(Comparator.comparing(StationResponse::getId))
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
}
