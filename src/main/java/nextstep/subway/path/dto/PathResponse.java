package nextstep.subway.path.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathStation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse(final List<StationResponse> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(final Path path) {
        return new PathResponse(StationResponse.ofList(path.getPathStations()), path.getDistance());
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

        public static StationResponse of(final PathStation pathStation) {
            return new StationResponse(pathStation.getId(), pathStation.getName(), pathStation.getCreatedAt());
        }

        public static List<StationResponse> ofList(final List<PathStation> pathStations) {
            return pathStations.stream()
                    .map(StationResponse::of)
                    .collect(Collectors.toList());
        }
    }
}
