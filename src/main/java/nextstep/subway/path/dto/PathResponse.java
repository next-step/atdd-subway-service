package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations = new ArrayList<>();
    private Integer distance;

    public PathResponse(List<StationResponse> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<StationResponse> stationsResponses, int distance) {
        return new PathResponse(stationsResponses, distance);
    }

    public List<Long> getStationIds() {
        return stations.stream()
                .map(station -> station.id)
                .collect(Collectors.toList());
    }

    public static class StationResponse {
        private Long id;
        private String name;
        private LocalDateTime createdAt;

        public StationResponse(Long id, String name, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        public static StationResponse of(Long id, String name, LocalDateTime createdAt) {
            return new StationResponse(id, name, createdAt);
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
