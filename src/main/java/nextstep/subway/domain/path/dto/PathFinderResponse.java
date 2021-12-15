package nextstep.subway.domain.path.dto;

import nextstep.subway.domain.path.domain.Route;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinderResponse {

    private List<Station> stations;
    private int distance;

    public PathFinderResponse() {
    }

    public PathFinderResponse(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static PathFinderResponse of(Route route) {
        List<Station> shortestRoute = route.getStations().stream()
                .map(Station::of)
                .collect(Collectors.toList());
        return new PathFinderResponse(shortestRoute, route.getDistance());
    }

    public static class Station {
        private Long id;
        private String name;
        private LocalDateTime createdAt;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public Station() {
        }

        public Station(final Long id, final String name, final LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        public static Station of(nextstep.subway.domain.station.domain.Station station) {
            return new Station(station.getId(), station.getName(), station.getCreatedDate());
        }
    }
}
