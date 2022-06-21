package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse upStation, StationResponse downStation) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getUpStation()), StationResponse.of(favorite.getUpStation()));
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }
}
