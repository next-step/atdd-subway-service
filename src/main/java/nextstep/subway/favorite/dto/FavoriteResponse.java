package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private List<StationResponse> stations;

    public FavoriteResponse() {}

    public FavoriteResponse(Long id, List<StationResponse> stations) {
        this.id = id;
        this.stations = stations;
    }

    public static FavoriteResponse of(Favorite favorite) {
        List<StationResponse> stations = favorite.findAllStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new FavoriteResponse(favorite.getId(), stations);
    }

    public Long getId() {
        return id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
