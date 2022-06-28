package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationInfo> stations;
    private int distance;
    private long fare;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, Distance distance, long fare) {
        this.stations = stations.stream()
                .map(StationInfo::new)
                .collect(Collectors.toList());
        this.distance = distance.getValue();
        this.fare = fare;
    }

    public List<StationInfo> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }

    public static class StationInfo {
        private Long id;
        private String name;
        private LocalDateTime createdAt;

        public StationInfo() {
        }

        public StationInfo(Long id) {
            this.id = id;
        }

        public StationInfo(Station station) {
            this.id = station.getId();
            this.name = station.getName();
            this.createdAt = station.getCreatedDate();
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
