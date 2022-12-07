package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

public class FavoritesResponse {
    private Long id;
    private List<StationResponse> stations;

    public FavoritesResponse(Long id, List<StationResponse> stations) {
        this.id = id;
        this.stations = stations;
    }

    public static FavoritesResponse of(Favorites favorites) {
        return new FavoritesResponse(favorites.getId(),
                Arrays.asList(StationResponse.of(favorites.getSourceStation()),
                        StationResponse.of(favorites.getTargetStation())));
    }

    public Long getId() {
        return id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
