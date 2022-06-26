package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.Objects;

public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(final Long id, final Station source, final Station target) {
        this.id = id;
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public static FavoriteResponse of(final Long id, final Station source, final Station target) {
        return new FavoriteResponse(id, source, target);
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
    public String toString() {
        return "FavoriteResponse{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                '}';
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
}
