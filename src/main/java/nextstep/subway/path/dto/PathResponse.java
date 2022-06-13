package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    public static class ShortestPath {
        private final Stations stations;
        private final int distance;

        public ShortestPath(List<Station> originalStations, int distance) {
            this.stations = new Stations(originalStations);
            this.distance = distance;
        }

        public Stations getStations() {
            return stations;
        }

        public int getDistance() {
            return distance;
        }
    }

    public static class Stations {
        private final List<PathStation> stations = new ArrayList<>();

        Stations(List<Station> originalStations) {
            originalStations.forEach(station -> this.stations.add(PathStation.of(station)));
        }

        public List<PathStation> getStations() {
            return stations;
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
