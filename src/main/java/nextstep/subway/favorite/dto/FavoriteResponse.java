package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite, List<Station> stations) {
        StationResponse source = toStationResponse(favorite, stations, s -> s.getId().equals(favorite.getSource()));
        StationResponse target = toStationResponse(favorite, stations, s -> s.getId().equals(favorite.getTarget()));
        return new FavoriteResponse(favorite.getId(), source, target);
    }

    public static List<FavoriteResponse> ofList(List<Favorite> favorites, List<Station> stations) {

        return favorites.stream()
                .map(f -> FavoriteResponse.of(f, stations))
                .collect(toList());
    }

    private static StationResponse toStationResponse(Favorite favorite, List<Station> stations, Predicate<Station> express) {
        return StationResponse.of(stations.stream()
                .filter(express)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."))
        );
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
