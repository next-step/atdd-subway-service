package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {
    private Long id;
    private FavoriteStation source;
    private FavoriteStation target;

    private FavoriteResponse() {
    }

    private FavoriteResponse(Long id, FavoriteStation source, FavoriteStation target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Long id, Station sourceStation, Station targetStation) {
        return new FavoriteResponse(
                id,
                FavoriteStation.of(sourceStation),
                FavoriteStation.of(targetStation));
    }

    public Long getId() {
        return id;
    }

    public FavoriteStation getSource() {
        return source;
    }

    public FavoriteStation getTarget() {
        return target;
    }
}
