package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = new StationResponse(source.getId(), source.getName(), source.getCreatedDate(),
                source.getModifiedDate());
        this.target = new StationResponse(target.getId(), target.getName(), target.getCreatedDate(),
                target.getModifiedDate());
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
