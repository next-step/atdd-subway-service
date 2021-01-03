package nextstep.subway.path.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse(final List<StationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    @Getter
    @NoArgsConstructor
    public static class StationResponse {

        private Long id;
        private String name;
        private LocalDateTime createdAt;

        private StationResponse(final Long id, final String name, final LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        public static StationResponse of(final Station station) {
            return new StationResponse(station.getId(), station.getName(), station.getCreatedDate());
        }
    }
}
