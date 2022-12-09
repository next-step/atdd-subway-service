package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

public class PathResponse {
    private List<PathStationResponse> stations;
    private int distance;

    public PathResponse(List<PathStationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(PathStationResponse.of(path.getStations()), path.getDistance());
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static class PathStationResponse {
        private Long id;
        private String name;
        private LocalDateTime createdAt;

        public PathStationResponse(Long id, String name, LocalDateTime createdDate) {
            this.id = id;
            this.name = name;
            this.createdAt = createdDate;
        }

        public static List<PathStationResponse> of(List<Station> stations) {
            return stations.stream()
                .map(station -> new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate()))
                .collect(Collectors.toList());
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
}
