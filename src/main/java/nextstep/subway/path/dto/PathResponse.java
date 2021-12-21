package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.SectionWeightedEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;
    private Fare fare;

    public PathResponse(List<StationResponse> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(GraphPath<Station, SectionWeightedEdge> path) {
        return new PathResponse(createStationList(path), path.getWeight());
    }

    private static List<StationResponse> createStationList(GraphPath<Station, SectionWeightedEdge> path) {
        return path.getVertexList()
                .stream()
                .map(station -> PathResponse.StationResponse.of(station.getId(), station.getName(), station.getCreatedDate()))
                .collect(Collectors.toList());
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

    public double getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
