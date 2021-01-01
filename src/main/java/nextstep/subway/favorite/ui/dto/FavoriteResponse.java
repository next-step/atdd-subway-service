package nextstep.subway.favorite.ui.dto;

import nextstep.subway.favorite.domain.adapters.SafeStationInFavorite;

import java.util.Objects;

public class FavoriteResponse {
    private final Long id;
    private final StationInFavoriteResponse source;
    private final StationInFavoriteResponse target;

    public FavoriteResponse(Long id, StationInFavoriteResponse source, StationInFavoriteResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Long id, SafeStationInFavorite source, SafeStationInFavorite target) {
        return new FavoriteResponse(id, new StationInFavoriteResponse(source), new StationInFavoriteResponse(target));
    }

    public Long getId() {
        return id;
    }

    public StationInFavoriteResponse getSource() {
        return source;
    }

    public StationInFavoriteResponse getTarget() {
        return target;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(source, that.source) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }

    @Override
    public String toString() {
        return "FavoriteResponse{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
