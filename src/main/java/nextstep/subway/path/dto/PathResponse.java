package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    public static class ShortestPath {
        private List<PathStation> stations = new ArrayList<>();
        private int distance;

        public ShortestPath() {}

        public ShortestPath(List<Station> originalStations, int distance) {
            originalStations.forEach(station -> this.stations.add(PathStation.of(station)));
            this.distance = distance;
        }

        public List<PathStation> getStations() {
            return stations;
        }

        public int getDistance() {
            return distance;
        }
    }

    public static class PathStation {
        private final Long id;
        private final String name;
        private final LocalDateTime createdAt;

        PathStation(Long id, String name, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        public static PathStation of(Station station) {
            return new PathStation(station.getId(), station.getName(), station.getCreatedDate());
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
