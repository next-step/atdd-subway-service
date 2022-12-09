package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(FavoritePair pair) {
        return new FavoriteResponse(pair.getId(),
            StationResponse.of(pair.getSource()),
            StationResponse.of(pair.getTarget()));
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

    public boolean matches(Long source, Long target) {
        return this.source.getId().equals(source)
            && this.target.getId().equals(target);
    }
}
