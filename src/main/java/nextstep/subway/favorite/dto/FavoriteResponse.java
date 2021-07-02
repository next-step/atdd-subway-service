package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.FavoriteSection;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(FavoriteSection favoriteSection) {
        return new FavoriteResponse(favoriteSection.getId(), StationResponse.of(favoriteSection.getSourceStation()), StationResponse.of(favoriteSection.getTargetStation()));
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
