package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.FavoritePath;
import nextstep.subway.station.dto.StationResponse;

public class FavoritePathResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoritePathResponse() {}

    private FavoritePathResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoritePathResponse from(FavoritePath favoritePath) {
        return new FavoritePathResponse(favoritePath.getId(),
                                        StationResponse.of(favoritePath.getSource()),
                                        StationResponse.of(favoritePath.getTarget()));
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
