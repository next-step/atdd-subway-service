package nextstep.subway.favorite.dto;

import java.util.Objects;

import nextstep.subway.favorite.domain.Favorite;
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

    public static FavoriteResponse from(Favorite favorite) {
        StationResponse source = StationResponse.from(favorite.getSource());
        StationResponse target = StationResponse.from(favorite.getTarget());

        return new FavoriteResponse(favorite.getId(), source, target);
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FavoriteResponse))
            return false;
        FavoriteResponse that = (FavoriteResponse)o;
        return Objects.equals(id, that.id) &&
            Objects.equals(source, that.source) &&
            Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
