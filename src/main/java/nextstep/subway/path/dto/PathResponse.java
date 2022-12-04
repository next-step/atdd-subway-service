package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PathResponse {
    private List<PathStationResponse> stations;
    private int distance;

    public PathResponse(List<PathStationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
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
