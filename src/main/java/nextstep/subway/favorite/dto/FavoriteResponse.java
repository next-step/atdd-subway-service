package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite, Station source, Station target) {
        this.id = favorite.getId();
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public static FavoriteResponse of(Favorite favorite, Station source, Station target) {
        return new FavoriteResponse(favorite, source, target);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite, favorite.getSourceStation(), favorite.getTargetStation());
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
