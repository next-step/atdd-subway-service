package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.Objects;

public class FavoriteSection {
    private long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteSection(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteSection from(Favorite favorite) {
        return new FavoriteSection(favorite.getId(), StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget()));
    }

    public static FavoriteSection of(long id, StationResponse source, StationResponse target) {
        return new FavoriteSection(id, source, target);
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public long getId() {
        return this.id;
    }

    public String sourceStationName() {
        return this.source.getName();
    }

    public String targetStationName() {
        return this.target.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteSection that = (FavoriteSection) o;
        return id == that.id && source.equals(that.source) && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
