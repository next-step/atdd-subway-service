package nextstep.subway.favorite.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private List<StationResponse> stations;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Station source, Station target) {
        this.stations = new ArrayList<>(Arrays.asList(source.toResponse(), target.toResponse()));
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getSource(), favorite.getTarget());
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
