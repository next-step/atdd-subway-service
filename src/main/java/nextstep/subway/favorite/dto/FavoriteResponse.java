package nextstep.subway.favorite.dto;

import java.util.Objects;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public Long getSourceId() {
        return source.getId();
    }

    public StationResponse getTarget() {
        return target;
    }

    public Long getTargetId() {
        return target.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(source, that.source)
                && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
