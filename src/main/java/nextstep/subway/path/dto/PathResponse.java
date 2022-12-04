package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
    private List<PathStationResponse> stations;
    private int distance;

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static class PathStationResponse {
        private Long id;
        private String name;
        private String createdAt;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
