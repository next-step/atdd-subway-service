package nextstep.subway.favorite.dto;

import java.util.Objects;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    Long id;
    StationResponse source;
    StationResponse target;

    private FavoriteResponse() {
    }

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Long id, StationResponse source, StationResponse target) {
        return new FavoriteResponse(id, source, target);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSource()), StationResponse.of(favorite.getTarget()));
    }

    public Long getId() {
        return this.id;
    }

    public StationResponse getSource() {
        return this.source;
    }

    public StationResponse getTarget() {
        return this.target;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FavoriteResponse)) {
            return false;
        }
        FavoriteResponse favoriteResponse = (FavoriteResponse) o;
        return Objects.equals(id, favoriteResponse.id) && Objects.equals(source, favoriteResponse.source) && Objects.equals(target, favoriteResponse.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
